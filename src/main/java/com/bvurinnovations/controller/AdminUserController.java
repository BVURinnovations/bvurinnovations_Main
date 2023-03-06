package com.bvurinnovations.controller;

import com.bvurinnovations.config.EndPointConfig;
import com.bvurinnovations.dto.AdminUserDTO;
import com.bvurinnovations.dto.LoginDTO;
import com.bvurinnovations.dto.ServiceDTO;
import com.bvurinnovations.dto.WorkspaceDTO;
import com.bvurinnovations.entity.WorkspaceEntity;
import com.bvurinnovations.service.AdminUserService;
import com.bvurinnovations.util.Constants;
import com.bvurinnovations.util.S3Utils;
import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(value = EndPointConfig.VERSION_1 + EndPointConfig.CONFIGURATOR)
public class AdminUserController {

    @Autowired
    AdminUserService adminUserService;


    @RequestMapping(value = EndPointConfig.LOGIN_OTP, method = RequestMethod.POST)
    public ResponseEntity<?> getUserDetails(@RequestBody LoginDTO loginDTO, @RequestParam(value = "resend", required = false) boolean resend) throws Exception {
        if (loginDTO == null || loginDTO.getCountryCode() == null || loginDTO.getMobile() == null) {
            throw new Exception("PHONE_NUMBER_OR_COUNTRY_CODE_MISSING");
        }
        ModelMap map = new ModelMap();
        map.put("id", adminUserService.getUserDetail(loginDTO, resend));
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @RequestMapping(value = EndPointConfig.VERIFY_OTP, method = RequestMethod.POST)
    public ResponseEntity<AdminUserDTO> verifyAdminUserOTP(@NotNull @PathVariable("id") String id, @NotNull @PathVariable("otp") Integer otp) throws Exception {

        return new ResponseEntity<>(adminUserService.verifyAdminUserOTP(id, otp), HttpStatus.OK);
    }

    @RequestMapping(value = EndPointConfig.REGISTER, method = RequestMethod.POST)
    public ResponseEntity<AdminUserDTO> registerAdminUser(@RequestBody AdminUserDTO dto) throws Exception {
        if (dto == null) {
            throw new Exception("USER_DTO_MISSING");
        }
        return new ResponseEntity<>(adminUserService.registerAdminUser(dto), HttpStatus.OK);
    }

    @RequestMapping(value = EndPointConfig.SERVICES, method = RequestMethod.GET)
    public ResponseEntity<List<ServiceDTO>> getServiceDetails(@NotNull @PathVariable(value = "userId") String userId) throws Exception {

        return new ResponseEntity<>(adminUserService.getServiceDetails(userId), HttpStatus.OK);
    }

    @RequestMapping(value = EndPointConfig.CREATE_WORKSPACE, method = RequestMethod.POST)
    public ResponseEntity<WorkspaceEntity> createWorkspace(@RequestBody WorkspaceDTO dto, @RequestParam(value = "userId")String userId) throws Exception {
        if (dto == null) {
            throw new Exception("USER_DTO_MISSING");
        }
        return new ResponseEntity<>(adminUserService.createWorkspace(dto, userId), HttpStatus.OK);
    }

    @RequestMapping(value = EndPointConfig.CREATE_WORKSPACE_IMAGE, method = RequestMethod.POST, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> uploadWorkSpaceImages(@NotNull @PathVariable(value = "workspaceId") String workspaceId, @RequestPart(value = "files") List<MultipartFile> files,
                                        @RequestParam(value = "userId")String userId) throws Exception {
        ModelMap map = new ModelMap();
        map.put("isUploaded",adminUserService.uploadWorkspaceImages(userId, files, workspaceId));
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @RequestMapping(value = EndPointConfig.MODIFY_WORKSPACE, method = RequestMethod.DELETE)
    public String deleteWorkspace(@PathVariable(value = "id") String id,
                                  @RequestParam(value = "userId")String userId) throws Exception {
        if (id == null) {
            throw new Exception("USER_ID_MISSING");
        }
        return adminUserService.markWorkspaceDeleted(id, userId);
    }

    @RequestMapping(value = EndPointConfig.MODIFY_WORKSPACE, method = RequestMethod.PUT)
    public String modifyWorkspace(@RequestBody WorkspaceDTO dto, @PathVariable(value = "id") String id,
                                  @RequestParam(value = "userId")String userId) throws Exception {
        if (dto == null) {
            throw new Exception("USER_DTO_MISSING");
        }
        return adminUserService.modifyWorkspace(dto, userId, id);
    }

    @RequestMapping(value = EndPointConfig.CREATE_DOCUMENTS_IMAGE, method = RequestMethod.POST, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> uploadCollaboratorDocuments(@RequestPart(value = "files") List<MultipartFile> files,
                                                   @RequestParam(value = "userId")String userId) throws Exception {
        ModelMap map = new ModelMap();
        map.put("isUploaded",adminUserService.uploadCollaboratorDocuments(userId, files));
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @RequestMapping(value = EndPointConfig.UPLOAD_ROLL, method = RequestMethod.POST, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> uploadRolls(@RequestPart(value = "file") MultipartFile file, @RequestPart("description") String description,
                                         @RequestParam(value = "userId")String userId) throws Exception {
        ModelMap map = new ModelMap();
        map.put("isUploaded",adminUserService.uploadRolls(userId, file, description));
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @RequestMapping(value = EndPointConfig.ROLL, method = RequestMethod.GET)
    public ResponseEntity<?> getRolls(@RequestParam(value = "userId")String userId) throws Exception {
        ModelMap map = new ModelMap();
        map.put("isUploaded",adminUserService.getRolls(userId));
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @RequestMapping(value = "test", method = RequestMethod.GET)
    public String test123() throws Exception {
        //S3Utils.getS3ClientWithCredentials(Constants.accessKey, Constants.secretKey);
        return "d";
    }
}
