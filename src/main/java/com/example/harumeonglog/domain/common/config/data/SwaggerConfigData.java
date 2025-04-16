package com.example.harumeonglog.domain.common.config.data;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "springdoc.swagger-ui")
public class SwaggerConfigData {
    private String path;
    private boolean enabled;
    private SwaggerAuthentication authentication;

    @Getter
    @Setter
    public static class SwaggerAuthentication {
        private String username;
        private String password;
        private List<String> profileScope;
    }
}
