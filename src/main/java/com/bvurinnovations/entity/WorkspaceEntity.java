package com.bvurinnovations.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Getter
@Setter
@Entity
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
    @Column(columnDefinition = "JSON")
    private String education;
    @Column(columnDefinition = "JSON")
    private String expertise;
    @Column(columnDefinition = "JSON")
    private String workplaceTime;
    private String createdBy;
    private String modifiedBy;
    private Date createdAt;
    private Date modifiedAt;
    private boolean isActive;
    @Column(columnDefinition = "JSON")
    private String upload;
    private int rate;
    private String status;
}
