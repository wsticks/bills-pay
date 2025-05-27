package com.interswitch.user_management.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties("usermanagement")
@Configuration
@Data
public class UserManagementProperties {

    private String jwtHeader;
    private String jwtAuth;
    private String jwtSecret;
    private String jwtExpiration;
    private String jwtRememberMeExpiration;
    private String jwtRefreshExpiration;
    private String jwtRouthAuthPath;
    private String jwtRouthAuthRefresh;
}