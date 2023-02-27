package com.bvurinnovations.service;

import com.bvurinnovations.dto.AdminUserDTO;
import com.bvurinnovations.dto.LoginDTO;
import com.bvurinnovations.dto.ServiceDTO;
import com.bvurinnovations.dto.WorkspaceDTO;
import com.bvurinnovations.entity.AdminOTPEntity;
import com.bvurinnovations.entity.AdminUserEntity;
import com.bvurinnovations.entity.ServiceEntity;
import com.bvurinnovations.entity.WorkspaceEntity;
import com.bvurinnovations.repository.AdminOTPRepository;
import com.bvurinnovations.repository.AdminUserRepository;
import com.bvurinnovations.repository.ServicesRepository;
import com.bvurinnovations.repository.WorkspaceRepository;
import com.bvurinnovations.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

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

    public String getUserDetail(LoginDTO loginDTO, boolean resend) {
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
            otpEntity = otpRepository.findByUserId(entity.getId());
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
    public String createWorkspace(WorkspaceDTO dto, String userId, List<MultipartFile> file) throws Exception {
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
        return "WORKSPACE_CREATED";
    }

    public String modifyWorkspace(WorkspaceDTO dto, String userId) throws Exception {
        AdminUserEntity userEntity = adminUserRepository.findAdminUserById(userId);
        if (userEntity == null) {
            throw new Exception("USER_NOT_FOUND");
        }
        WorkspaceEntity entity = mapWorkspaceDTO(dto);
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
        entity.setEducation(dto.getEducation());
        entity.setExpertise(dto.getExpertise());
        entity.setWorkplaceTime(dto.getWorkplaceTime());
        entity.setUpload(dto.getUpload());
        entity.setRate(dto.getRate());
        return entity;
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
}
