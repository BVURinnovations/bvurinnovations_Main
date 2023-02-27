package com.bvurinnovations.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class AdminUserDTO {
    private String id;
    private String email;
    private String countryCode;
    private String mobile;
    private String firstName;
    private String lastName;
    private boolean isVerified;
}
