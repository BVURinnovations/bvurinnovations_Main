package com.bvurinnovations.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.bvurinnovations.dto.AdminUserDTO;
import com.bvurinnovations.dto.LoginDTO;
import com.bvurinnovations.dto.ServiceDTO;
import com.bvurinnovations.dto.WorkspaceDTO;
import com.bvurinnovations.entity.*;
import com.bvurinnovations.repository.*;
import com.bvurinnovations.util.Constants;
import com.bvurinnovations.util.OTPUtil;
import com.bvurinnovations.util.S3Utils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.*;

@Service
public class AdminUserService {
    @Autowired
    AdminUserRepository adminUserRepository;

    @Autowired
    AdminOTPRepository otpRepository;

    @Autowired
    ServicesRepository servicesRepository;

    @Autowired
    WorkspaceRepository workspaceRepository;

    @Autowired
    RollRepository rollRepository;

    OTPUtil otpUtil = new OTPUtil();

    private String jsonData;

    public String getUserDetail(LoginDTO loginDTO, boolean resend) throws UnirestException {
        String otpType = Constants.LOGIN_OTP;
        AdminUserEntity entity = adminUserRepository.findAdminUserByMobile(loginDTO.getMobile());
        if (entity == null) {
            entity = new AdminUserEntity();
            entity.setMobile(loginDTO.getMobile());
            entity.setCountryCode(loginDTO.getCountryCode());
            entity.setVerified(false);
            entity.setCreatedAt(new Date());
            adminUserRepository.save(entity);
            otpType = Constants.REGISTER_OTP;
        }
        Random random = new Random();
        int number = random.nextInt(999999);
        AdminOTPEntity otpEntity;
        if (resend) {
            otpEntity = otpRepository.findOTPByUserId(entity.getId());
            otpType = Constants.RESEND_OTP;
        } else {
            otpEntity = new AdminOTPEntity();
            otpEntity.setUserId(entity.getId());
        }
        otpEntity.setOtp(number);
        otpEntity.setType(otpType);
        otpEntity.setVerified(false);
        otpEntity.setCreatedAt(new Date());
        otpRepository.save(otpEntity);
        String mobile = entity.getCountryCode() + entity.getMobile();
        otpUtil.sendOTPWithTemplate(mobile, Constants.LOGIN_OTP_TEMPLATE, number);

        return entity.getId();
    }

    public AdminUserDTO verifyAdminUserOTP(String id, int otp) throws Exception {
        AdminUserEntity user = adminUserRepository.findAdminUserById(id);
        if (user == null) {
            throw new Exception("USER_NOT_FOUND");
        }
        AdminOTPEntity otpEntity = otpRepository.findByOTPAndUserId(otp, id);
        if (otpEntity == null) {
            throw new Exception("INVALID_OTP");
        }
        otpRepository.delete(otpEntity);
        return setUserEntity(user);
    }

    public AdminUserDTO registerAdminUser(AdminUserDTO dto) throws Exception {
        AdminUserEntity user = adminUserRepository.findAdminUserById(dto.getId());
        if (user == null) {
            throw new Exception("USER_NOT_FOUND");
        }
        user.setEmail(dto.getEmail());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setVerified(true);
        adminUserRepository.save(user);
        return setUserEntity(user);
    }

    private AdminUserDTO setUserEntity(AdminUserEntity user) {
        AdminUserDTO dto = new AdminUserDTO();
        dto.setId(user.getId());
        dto.setCountryCode(user.getCountryCode());
        dto.setMobile(user.getMobile());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setVerified(user.isVerified());

        return dto;
    }

    public List<ServiceDTO> getServiceDetails(String userId) throws Exception {
        AdminUserEntity userEntity = adminUserRepository.findAdminUserById(userId);
        if (userEntity == null) {
            throw new Exception("USER_NOT_FOUND");
        }
        List<ServiceDTO> dtoList = null;
        List<ServiceEntity> entities = servicesRepository.findAll();
        if (entities.size() > 0) {
             dtoList = new ArrayList<>();
            for (ServiceEntity entity : entities) {
                ServiceDTO dto = new ServiceDTO();
                dto.setId(entity.getId());
                dto.setActive(entity.isActive());
                dto.setName(entity.getName());
                dtoList.add(dto);
            }
        }
        return dtoList;
    }
    public WorkspaceEntity createWorkspace(WorkspaceDTO dto, String userId) throws Exception {
        AdminUserEntity userEntity = adminUserRepository.findAdminUserById(userId);
        if (userEntity == null) {
            throw new Exception("USER_NOT_FOUND");
        }
        WorkspaceEntity entity = mapWorkspaceDTO(dto);
        entity.setCreatedBy(userEntity.getId());
        entity.setCreatedAt(new Date());
        entity.setUserId(dto.getUserId());
        entity.setActive(true);
        entity.setStatus(Constants.ACTIVE);
        workspaceRepository.save(entity);
        return entity;
    }

    public boolean uploadWorkspaceImages(String userId, List<MultipartFile> files, String workspaceId) throws Exception {
        WorkspaceEntity entity = workspaceRepository.findWorkspaceByIdAndUserId(workspaceId, userId);
        String location = "collaborator/"  + userId + "/workspaceImages/";
        JSONObject jsonObject = new JSONObject();
        if (entity != null) {

        } else {
            throw new Exception("WORKSPACE_NOT_FOUND");
        }

        AdminUserEntity userEntity = adminUserRepository.findAdminUserById(userId);
        if (userEntity == null) {
            throw new Exception("USER_NOT_FOUND");
        }
        AmazonS3 s3Client =S3Utils.getS3Client();
        for (MultipartFile multiPart : files) {

            File file = convert(multiPart);
            jsonObject.put(file.toString(),location + file);
            s3Client.putObject("ap-sounth-1-dev-furrcrew", location + file, file);

        }
        entity.setUpload(jsonObject.toString());
        return true;
    }

    public boolean uploadCollaboratorDocuments(String userId, List<MultipartFile> files) throws Exception {
        AdminUserEntity userEntity = adminUserRepository.findAdminUserById(userId);
        if (userEntity == null) {
            throw new Exception("USER_NOT_FOUND");
        }
        AmazonS3 s3Client =S3Utils.getS3Client();
        for (MultipartFile multiPart : files) {
            File file = convert(multiPart);
            s3Client.putObject("ap-sounth-1-dev-furrcrew", "collaborator/"  + userId + "/documents" + "/" + file, file);

        }
        return true;
    }

    public boolean uploadRolls(String userId, MultipartFile file, String description) throws Exception {
        AdminUserEntity userEntity = adminUserRepository.findAdminUserById(userId);
        if (userEntity == null) {
            throw new Exception("USER_NOT_FOUND");
        }
        AmazonS3 s3Client =S3Utils.getS3Client();
        File uploadFile = convert(file);
        String path = "rolls" + "/" + uploadFile;
        RollEntity rollEntity = new RollEntity();
        rollEntity.setDescription(description);
        rollEntity.setCreatedAt(new Date());
        rollEntity.setStatus(Constants.ACTIVE);
        rollEntity.setLocation(path);
        rollEntity.setCreatedBy(userId);
        rollEntity.setLikeCount(0);
        rollEntity.setSharedCount(0);
        rollRepository.save(rollEntity);
        String s= "";
        s3Client.putObject("ap-sounth-1-dev-furrcrew", path, uploadFile);

        return true;
    }

    public List<String> getRolls(String userId) {
        AmazonS3 s3Client =S3Utils.getS3Client();
        List<RollEntity> rolls = rollRepository.getRollsByUserId(userId);
        List<String> list = new ArrayList<>();
        for (RollEntity entity : rolls) {
            //S3Object s3Object = s3Client.getObject("ap-sounth-1-dev-furrcrew", "rolls/sample-mp4-file-small.mp4");
            Calendar currentTimeNow = Calendar.getInstance();
            currentTimeNow.add(Calendar.MINUTE, 10);
            Date date = currentTimeNow.getTime();
            URL url = s3Client.generatePresignedUrl("ap-sounth-1-dev-furrcrew", entity.getLocation(), date);
            list.add(url.toString());
        }
        return list;
    }

    public String modifyWorkspace(WorkspaceDTO dto, String userId, String id) throws Exception {
        AdminUserEntity userEntity = adminUserRepository.findAdminUserById(userId);
        if (userEntity == null) {
            throw new Exception("USER_NOT_FOUND");
        }
        WorkspaceEntity entity = workspaceRepository.findWorkspaceByIdAndUserId(id, userId);
        if (entity == null) {
            throw new Exception("WORKSPACE_NOT_FOUND");
        }
        entity = mapWorkspaceDTO(dto);
        entity.setModifiedBy(userEntity.getId());
        return "WORKSPACE_MODIFIED";
    }

    private WorkspaceEntity mapWorkspaceDTO(WorkspaceDTO dto) {
        WorkspaceEntity entity = new WorkspaceEntity();
        entity.setDesignationName(dto.getDesignationName());
        entity.setAbout(dto.getAbout());
        entity.setWorkplaceName(dto.getWorkplaceName());
        entity.setServiceId(dto.getServiceId());
        entity.setAddress(dto.getAddress());
        entity.setTown(dto.getTown());
        entity.setCity(dto.getCity());
        entity.setPincode(dto.getPincode());
        entity.setState(dto.getState());
        String education = setJsonData(dto.getEducation().toString());
        entity.setEducation(education);
        entity.setExpertise(setJsonData(dto.getExpertise().toString()));
        entity.setWorkplaceTime(setJsonData(dto.getWorkplaceTime().toString()));
        entity.setUpload(setJsonData(dto.getUpload().toString()));
        entity.setRate(dto.getRate());
        return entity;
    }

    public String setJsonData(String jsonData) {
        // Method parameter jsonData is simply ignored
        try {
            return this.jsonData = new ObjectMapper().writeValueAsString(jsonData);
        } catch (JsonProcessingException e) {
            System.out.println(e);
        }
        return null;
    }

    public String markWorkspaceDeleted(String workspaceId, String userId) throws Exception {
        AdminUserEntity userEntity = adminUserRepository.findAdminUserById(userId);
        if (userEntity == null) {
            throw new Exception("USER_NOT_FOUND");
        }
        WorkspaceEntity entity = workspaceRepository.findWorkspaceByIdAndUserId(workspaceId, userId);
        entity.setStatus(Constants.DELETED);
        entity.setActive(false);
        workspaceRepository.save(entity);
        return "WORKSPACE_DELETED";
    }

    public void uploadRoll(List<MultipartFile> file) {
    }

    private File convert(MultipartFile file) {
        File convFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        try {
            convFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(convFile);
            fos.write(file.getBytes());
            fos.close(); //IOUtils.closeQuietly(fos);
        } catch (IOException e) {
            convFile = null;
        }

        return convFile;
    }
}
