package com.bvurinnovations.controller;

import com.bvurinnovations.config.EndPointConfig;
import com.bvurinnovations.dto.AdminUserDTO;
import com.bvurinnovations.dto.LoginDTO;
import com.bvurinnovations.dto.WorkspaceDTO;
import com.bvurinnovations.service.AdminUserService;
import com.bvurinnovations.util.Constants;
import com.bvurinnovations.util.S3Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping(value = EndPointConfig.VERSION_1 + EndPointConfig.CONFIGURATOR)
public class AdminUserController {

    @Autowired
    AdminUserService adminUserService;


    @RequestMapping(value = EndPointConfig.LOGIN_OTP, method = RequestMethod.POST)
    public String getUserDetails(@RequestBody LoginDTO loginDTO, @RequestParam(value = "resend", required = false) boolean resend) throws Exception {
        if (loginDTO == null || loginDTO.getCountryCode() == null || loginDTO.getMobile() == null) {
            throw new Exception("PHONE_NUMBER_OR_COUNTRY_CODE_MISSING");
        }
        return adminUserService.getUserDetail(loginDTO, resend);
    }

    @RequestMapping(value = EndPointConfig.VERIFY_OTP, method = RequestMethod.POST)
    public AdminUserDTO verifyAdminUserOTP(@PathVariable("id") String id, @PathVariable("otp") Integer otp) throws Exception {
        if (id == null || id.isEmpty()) {
            throw new Exception("USER_ID_OR_OTP_MISSING");
        }
        return adminUserService.verifyAdminUserOTP(id, otp);
    }

    @RequestMapping(value = EndPointConfig.REGISTER, method = RequestMethod.POST)
    public AdminUserDTO verifyAdminUserOTP(@RequestBody AdminUserDTO dto) throws Exception {
        if (dto == null) {
            throw new Exception("USER_DTO_MISSING");
        }
        return adminUserService.registerAdminUser(dto);
    }

    @RequestMapping(value = EndPointConfig.CREATE_WORKSPACE, method = RequestMethod.POST)
    public String createWorkspace(@RequestBody WorkspaceDTO dto, @RequestPart("file") List<MultipartFile> files,
                                        @RequestParam(value = "userId")String userId) throws Exception {
        if (dto == null) {
            throw new Exception("USER_DTO_MISSING");
        }
        return adminUserService.createWorkspace(dto, userId, files);
    }

    @RequestMapping(value = "test", method = RequestMethod.GET)
    public String test123() throws Exception {
        S3Utils.getS3ClientWithCredentials(Constants.accessKey, Constants.secretKey);
        return "d";
    }
}
