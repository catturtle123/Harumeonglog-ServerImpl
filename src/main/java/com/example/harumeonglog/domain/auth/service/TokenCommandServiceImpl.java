package com.example.harumeonglog.domain.auth.service;

import com.example.harumeonglog.domain.auth.converter.AuthConverter;
import com.example.harumeonglog.domain.auth.dto.response.AuthResponse;
import com.example.harumeonglog.global.security.domain.CustomUserDetails;
import com.example.harumeonglog.global.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenCommandServiceImpl implements TokenCommandService {

    private final JwtUtil jwtUtil;
    private final RedisCommandService redisCommandService;

    @Override
    public AuthResponse.AuthLoginResponse createToken(CustomUserDetails userDetails) {
        String accessToken = jwtUtil.createAccessToken(userDetails);
        String refreshToken = jwtUtil.createRefreshToken(userDetails);
        redisCommandService.addRefreshToken(userDetails.getLoginMember().getId(), refreshToken);
        return AuthConverter.toAuthLoginResponse(userDetails.getLoginMember().getId(), accessToken, refreshToken);
    }

    @Override
    public String createAccessToken(CustomUserDetails userDetails) {
        return jwtUtil.createAccessToken(userDetails);
    }
}
