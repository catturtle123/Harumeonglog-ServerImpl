package com.example.harumeonglog.domain.common.config.data;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "spring.config.activate")
public class ProfileConfigData {

    private String onProfile;
}
