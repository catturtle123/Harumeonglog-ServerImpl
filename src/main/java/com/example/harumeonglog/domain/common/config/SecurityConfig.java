package com.example.harumeonglog.domain.common.config;

import com.example.harumeonglog.domain.common.auth.filter.AbstractTokenFilter;
import com.example.harumeonglog.domain.common.auth.filter.AbstractTokenLogoutFilter;
import com.example.harumeonglog.domain.common.auth.filter.JwtTokenFilter;
import com.example.harumeonglog.domain.common.auth.filter.JwtTokenLogoutFilter;
import com.example.harumeonglog.domain.common.auth.handler.CustomAccessDeniedHandler;
import com.example.harumeonglog.domain.common.auth.handler.CustomAuthorizationEntryPoint;
import com.example.harumeonglog.domain.common.auth.handler.JwtTokenLogoutHandler;
import com.example.harumeonglog.domain.common.auth.service.CustomDetailService;
import com.example.harumeonglog.domain.common.auth.util.JwtUtil;
import com.example.harumeonglog.domain.common.auth.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextRepository;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity(debug = true)
public class SecurityConfig {

    private final RedisUtil redisUtil;
    private final JwtUtil jwtUtil;
    private final CustomDetailService customDetailService;
    private final JwtTokenLogoutHandler jwtTokenLogoutHandler;
    private final CustomAuthorizationEntryPoint customAuthorizationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    private final String[] allowUrl = {
            "/login",
            "/",
            "/swagger-ui/**",
            "/swagger-resources/**",
            "/v3/api-docs/**",
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests(request -> request
                        .requestMatchers(allowUrl).permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtTokenFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtTokenLogoutFilter(), JwtTokenFilter.class)
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(customAuthorizationEntryPoint)
                        .accessDeniedHandler(customAccessDeniedHandler)
                )
                .csrf(CsrfConfigurer::disable)
                .formLogin(FormLoginConfigurer::disable)
                .httpBasic(HttpBasicConfigurer::disable)
        ;

        return http.build();
    }

    @Bean
    AbstractTokenFilter jwtTokenFilter() {
        return new JwtTokenFilter(jwtUtil, redisUtil, customDetailService);
    }

    @Bean
    AbstractTokenLogoutFilter jwtTokenLogoutFilter() {
        return new JwtTokenLogoutFilter(jwtTokenFilter(), jwtTokenLogoutHandler, redisUtil, jwtUtil);
    }

    @Bean
    SecurityContextRepository securityContextRepository() {
        return jwtTokenFilter().getSecurityContextRepository();
    }
}
