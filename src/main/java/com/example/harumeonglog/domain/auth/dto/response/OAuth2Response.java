package com.example.harumeonglog.domain.auth.dto.response;

import com.example.harumeonglog.global.security.domain.CustomUserDetails;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class OAuth2Response {

    @Getter
    public static class OAuth2PublicKeyListResponse {
        private List<OAuth2PublicKeyResponse> keys;
    }

    @Getter
    public static class OAuth2PublicKeyResponse {
        private String kid;
        private String kty;
        private String alg;
        private String use;
        private String n;
        private String e;
    }

    @Getter
    public static class OAuth2IdTokenHeader {
        private String kid;
        private String typ;
        private String alg;
    }

    @Getter
    @Builder
    public static class OAuth2LoginSuccessResponse {
        private CustomUserDetails userDetails;
        private Boolean isSignUp;
    }
}
