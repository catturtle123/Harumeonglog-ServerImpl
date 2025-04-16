package com.example.harumeonglog.domain.member.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

public class MemberResponse {

    @Getter
    @Builder
    public static class MemberInfoResponse {
        private Long memberId;
        private String email;
        private String nickname;
        private String image;
    }

    @Getter
    @Builder
    public static class MemberLoginResponse {
        private Long memberId;
        private String accessToken;
        private String refreshToken;
    }

    @Getter
    @Builder
    public static class MemberLogoutResponse {
        private Long memberId;
    }

    @Getter
    @Builder
    public static class MemberInfoUpdateResponse {
        private Long memberId;
        private String image;
        private String nickname;
        private LocalDate birth;
    }
}
