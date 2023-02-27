package com.bvurinnovations.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "admin_user")
public class AdminUserEntity {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    private String email;
    private String countryCode;
    private String mobile;
    private String firstName;
    private String lastName;
    private Date createdAt;
    private Date modifiedAt;
    private boolean isVerified;

}
