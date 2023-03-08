package com.bvurinnovations.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class WorkspaceDTO implements Serializable {
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
    private Object services;
    private Object education;
    private Object expertise;
    private Object workplaceTime;
    private String createdBy;
    private String modifiedBy;
    private Date createdAt;
    private Date modifiedAt;
    private boolean isActive;
    private Object upload;
    private String status;
}
