package com.bvurinnovations.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class WorkspaceDTO {
    private String id;
    private String designationName;
    private String about;
    private String workplaceName;
    private String userId;
    private String serviceId;
    private String address;
    private String town;
    private String city;
    private int pincode;
    private String state;
    private String education;
    private String expertise;
    private String workplaceTime;
    private String createdBy;
    private String modifiedBy;
    private Date createdAt;
    private Date modifiedAt;
    private boolean isActive;
    private String upload;
    private int rate;
    private String status;
}
