package com.example.harumeonglog.global.security;

import com.example.harumeonglog.global.security.filter.AbstractTokenFilter;
import com.example.harumeonglog.global.security.filter.AbstractTokenLogoutFilter;
import com.example.harumeonglog.global.security.filter.JwtTokenFilter;
import com.example.harumeonglog.global.security.filter.JwtTokenLogoutFilter;
import com.example.harumeonglog.global.security.handler.CustomAccessDeniedHandler;
import com.example.harumeonglog.global.security.handler.CustomAuthorizationEntryPoint;
import com.example.harumeonglog.global.security.handler.JwtTokenLogoutHandler;
import com.example.harumeonglog.global.security.service.CustomDetailService;
import com.example.harumeonglog.global.util.JwtUtil;
import com.example.harumeonglog.global.util.RedisUtil;

import com.example.harumeonglog.global.data.ProfileConfigData;
import com.example.harumeonglog.global.data.SwaggerConfigData;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextRepository;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final SwaggerConfigData swaggerConfigData;
    private final ProfileConfigData profileConfigData;
    private final RedisUtil redisUtil;
    private final JwtUtil jwtUtil;
    private final CustomDetailService customDetailService;
    private final JwtTokenLogoutHandler jwtTokenLogoutHandler;
    private final CustomAuthorizationEntryPoint customAuthorizationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    private final String[] allowUrl = {
            "/health",
            "/members/*/login"
    };

    private final String[] apiUrl = {
            "/swagger-ui/**",
            "/swagger-resources/**",
            "/v3/api-docs/**",
            "/docs/**",
    };

    private final String[] formLoginUrl = {
            "/login",
            "/default-ui.css"
    };

    @Bean
    @Order(1)
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
    @Order(0)
    public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
        if (!swaggerConfigData.getAuthentication().getProfileScope().contains(profileConfigData.getOnProfile())) {

            http.securityMatchers(matchers -> matchers.requestMatchers(apiUrl))
                    .authorizeHttpRequests(request -> request.anyRequest().permitAll())
            ;
            return http.build();
        }
        http
                .securityMatchers(matchers -> matchers
                        .requestMatchers(apiUrl)
                        .requestMatchers(formLoginUrl)
                )
                .authorizeHttpRequests(request -> request
                        .requestMatchers(formLoginUrl).permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(Customizer.withDefaults())
                .userDetailsService(inMemoryUserDetailsManager())
                .sessionManagement(management -> management
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                )
                .csrf(CsrfConfigurer::disable)
                .httpBasic(HttpBasicConfigurer::disable)
                ;

        return http.build();
    }

    @Bean
    public UserDetailsService inMemoryUserDetailsManager() {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        UserDetails userDetails = User.builder()
                .username(swaggerConfigData.getAuthentication().getUsername())
                .password(passwordEncoder().encode(swaggerConfigData.getAuthentication().getPassword()))
                .roles("API")
                .build();
        manager.createUser(userDetails);
        return manager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
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
