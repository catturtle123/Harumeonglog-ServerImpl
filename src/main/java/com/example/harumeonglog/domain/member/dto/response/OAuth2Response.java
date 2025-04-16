package com.example.harumeonglog.domain.member.dto.response;

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
}
