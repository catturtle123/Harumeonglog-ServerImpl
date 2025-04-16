package com.example.harumeonglog.global.security.handler;

import com.example.harumeonglog.global.error.code.AuthErrorCode;
import com.example.harumeonglog.global.common.response.util.HttpResponseUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthorizationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        HttpResponseUtil.writeResponse(response, AuthErrorCode.FAIL_AUTHENTICATION, authException.getMessage());
    }

}
