package com.example.harumeonglog.domain.auth.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

public class AuthRequest {

    @Getter
    @Builder
    public static class AuthLoginRequest {
        private final String idToken;

        public AuthLoginRequest(
                @JsonProperty("idToken") String idToken
        ) {
            this.idToken = idToken;
        }

    }

    @Getter
    @Builder
    public static class AuthAccessReissueRequest {
        private final String refreshToken;

        public AuthAccessReissueRequest(
                @JsonProperty("refreshToken") String refreshToken
        ) {
            this.refreshToken = refreshToken;
        }
    }
}
