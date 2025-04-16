package com.example.harumeonglog.global.data;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "spring.security.oauth2.kakao")
public class KakaoOAuthConfigData {
    private String publicKeyUrl;
    private String iss;
    private String appKey;
}
