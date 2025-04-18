package com.example.harumeonglog.domain.auth.service;

import com.example.harumeonglog.domain.auth.dto.response.AuthResponse;
import com.example.harumeonglog.global.security.domain.CustomUserDetails;

public interface TokenCommandService {
    AuthResponse.AuthLoginResponse createToken(CustomUserDetails userDetails);
    String createAccessToken(CustomUserDetails userDetails);
}
