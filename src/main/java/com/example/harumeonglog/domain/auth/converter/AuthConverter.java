package com.example.harumeonglog.domain.auth.converter;

import com.example.harumeonglog.domain.auth.dto.response.AuthResponse;

public class AuthConverter {
    public static AuthResponse.AuthLoginResponse toAuthLoginResponse(Long memberId, String accessToken, String refreshToken) {
        return AuthResponse.AuthLoginResponse.builder()
                .memberId(memberId)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public static AuthResponse.AuthAccessReissueResponse toAuthAccessReissueResponse(String accessToken) {
        return AuthResponse.AuthAccessReissueResponse.builder()
                .accessToken(accessToken)
                .build();
    }
}
