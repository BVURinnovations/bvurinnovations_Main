package com.bvurinnovations.entity;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.databind.util.JSONPObject;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "workspace")
public class WorkspaceEntity {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
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
    @Column(name = "education", columnDefinition = "JSON")
    @JsonRawValue
    private String education;
    @Column(name = "expertise", columnDefinition = "JSON")
    @JsonRawValue
    private String expertise;
    @Column(columnDefinition = "JSON")
    @JsonRawValue
    private String workplaceTime;
    private String createdBy;
    private String modifiedBy;
    private Date createdAt;
    private Date modifiedAt;
    private boolean isActive;
    @Column(name = "upload", columnDefinition = "JSON")
    @JsonRawValue
    private String upload;
    @Column(name = "services", columnDefinition = "JSON")
    @JsonRawValue
    private String services;
    private String status;
}
