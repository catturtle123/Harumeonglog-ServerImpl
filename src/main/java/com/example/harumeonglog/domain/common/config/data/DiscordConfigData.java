package com.example.harumeonglog.domain.common.config.data;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "discord")
public class DiscordConfigData {

    private String webHookUrl;
}
