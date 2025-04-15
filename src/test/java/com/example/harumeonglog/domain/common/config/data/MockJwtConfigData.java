package com.example.harumeonglog.domain.common.config.data;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@TestConfiguration
public class MockJwtConfigData {

    private final String testSecret = "testtesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttest";
    private final long accessExpiration = 100000L;
    private final long refreshExpiration = 10000000L;

    @Bean
    public JwtConfigData jwtConfigData() {
        JwtConfigData.JwtTime jwtTime = new JwtConfigData.JwtTime();
        jwtTime.setAccess(accessExpiration);
        jwtTime.setRefresh(refreshExpiration);
        JwtConfigData jwtConfigData = new JwtConfigData();
        jwtConfigData.setTime(jwtTime);
        jwtConfigData.setSecret(testSecret);
        return jwtConfigData;
    }

}
