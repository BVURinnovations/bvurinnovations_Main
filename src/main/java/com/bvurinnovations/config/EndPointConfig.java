package com.bvurinnovations.config;

public class EndPointConfig {
    public static final String VERSION_1 = "/api/v1";
    public static final String CONFIGURATOR = "/admin";
    public static final String LOGIN_OTP = "/otp";
    public static final String VERIFY_OTP = "/verify/otp/{id}/{otp}";
    public static final String REGISTER = "/register";
    public static final String CREATE_WORKSPACE = "/workspace/create";
    public static final String CREATE_WORKSPACE_IMAGE = "/workspace/{workspaceId}/create/images";
    public static final String SERVICES = "/services/{userId}";
    public static final String MODIFY_WORKSPACE = "/workspace/{id}/modify";
    public static final String CREATE_DOCUMENTS_IMAGE = "/collaborator/documents/images";
    public static final String UPLOAD_ROLL = "/upload/roll";
    public static final String ROLL = "/rolls";
}
