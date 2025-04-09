package com.example.harumeonglog.domain.common.auth.handler;

import com.example.harumeonglog.domain.common.controller.code.GeneralSuccessCode;
import com.example.harumeonglog.domain.common.controller.response.util.HttpResponseUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtTokenLogoutHandler implements LogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        HttpResponseUtil.writeResponse(response, GeneralSuccessCode._OK, "로그아웃에 성공했습니다.");
    }
}
