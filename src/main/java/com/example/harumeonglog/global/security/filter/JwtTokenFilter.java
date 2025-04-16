package com.example.harumeonglog.global.security.filter;


import com.example.harumeonglog.global.error.code.AuthErrorCode;
import com.example.harumeonglog.global.security.service.CustomDetailService;
import com.example.harumeonglog.global.security.util.JwtUtil;
import com.example.harumeonglog.global.security.util.RedisUtil;
import com.example.harumeonglog.global.common.response.CustomResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;

@Slf4j
public class JwtTokenFilter extends AbstractTokenFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";
    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;
    private final CustomDetailService customDetailService;

    public JwtTokenFilter(JwtUtil jwtUtil, RedisUtil redisUtil, CustomDetailService customDetailService) {
        super(AUTHORIZATION_HEADER, TOKEN_PREFIX);
        this.jwtUtil = jwtUtil;
        this.redisUtil = redisUtil;
        this.customDetailService = customDetailService;
    }

    @Override
    protected boolean validToken(String token) {
        return jwtUtil.isValid(token) && !redisUtil.isBlackList(token);
    }

    @Override
    protected Authentication createAuthentication(String token) throws AuthenticationException {
        String username = jwtUtil.getUsername(token);
        if (username == null) {
            throw new BadCredentialsException("토큰에서 사용자 정보를 찾을 수 없습니다.");
        }

        UserDetails userDetails = customDetailService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, token, userDetails.getAuthorities());
        authentication.setDetails(userDetails);
        return authentication;
    }

    @Override
    protected void handleException(HttpServletRequest request, HttpServletResponse response, Exception e) throws IOException, ServletException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        log.info("로그인 실패 ({}): {}", e.getClass(), e.getMessage());
        ObjectMapper om = new ObjectMapper();
        om.writeValue(response.getOutputStream(), CustomResponse.fail(AuthErrorCode.FAIL_AUTHORIZATION, e.getMessage()));
    }
}
