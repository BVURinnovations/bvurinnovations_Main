package com.bvurinnovations.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ServiceDTO {
    private String id;
    private String name;
    private Date createdAt;
    private Date modifiedAt;
    private String createdBy;
    private String modifiedBy;
    private boolean isActive;
}
