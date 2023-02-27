package com.bvurinnovations.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginDTO {
    private String id;
    private String countryCode;
    private String mobile;
}
