package com.example.harumeonglog.domain.auth.dto.response;

import lombok.Builder;
import lombok.Getter;

public class AuthResponse {

    @Getter
    @Builder
    public static class AuthLoginResponse {
        private Long memberId;
        private String accessToken;
        private String refreshToken;
        private Boolean isSignUp;
    }

    @Getter
    @Builder
    public static class AuthLogoutResponse {
        private Long memberId;
    }

    @Getter
    @Builder
    public static class AuthAccessReissueResponse {
        private String accessToken;
    }
}
