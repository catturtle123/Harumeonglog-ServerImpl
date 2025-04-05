package com.example.harumeonglog.domain.common.auth.handler;

import com.example.harumeonglog.domain.common.auth.exception.AuthErrorCode;
import com.example.harumeonglog.domain.common.controller.response.util.HttpResponseUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        HttpResponseUtil.writeResponse(response, AuthErrorCode.FAIL_AUTHORIZATION, accessDeniedException.getMessage());
    }
}
