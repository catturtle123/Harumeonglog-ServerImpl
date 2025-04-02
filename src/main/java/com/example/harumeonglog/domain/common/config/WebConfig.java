package com.example.harumeonglog.domain.common.config;

import com.example.harumeonglog.domain.common.auth.annotation.resolver.AuthenticatedMemberResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {


    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authenticatedMemberResolver());
    }

    @Bean
    HandlerMethodArgumentResolver authenticatedMemberResolver() {
        return new AuthenticatedMemberResolver(new RequestAttributeSecurityContextRepository());
    }
}
