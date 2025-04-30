package com.example.harumeonglog.domain.auth.service;

import com.example.harumeonglog.domain.auth.dto.request.AuthRequest;
import com.example.harumeonglog.domain.auth.dto.response.AuthResponse;
import com.example.harumeonglog.domain.member.entity.Member;

public interface AuthCommandService {
    AuthResponse.AuthLoginResponse login(String provider, AuthRequest.AuthLoginRequest request);
    String logout(Member member);
    AuthResponse.AuthAccessReissueResponse reissueAccess(AuthRequest.AuthAccessReissueRequest request);
}
