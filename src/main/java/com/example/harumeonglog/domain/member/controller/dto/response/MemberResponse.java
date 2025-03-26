package com.example.harumeonglog.domain.member.controller.dto.response;

import com.example.harumeonglog.domain.member.domain.Member;
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

        public static MemberInfoResponse from(Member member) {
            return MemberInfoResponse.builder()
                    .memberId(member.getId())
                    .email(member.getEmail())
                    .nickname(member.getNickname())
                    .image(member.getImage())
                    .build();
        }
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

        public static MemberInfoUpdateResponse from(Member member) {
            return MemberInfoUpdateResponse.builder()
                    .memberId(member.getId())
                    .image(member.getImage())
                    .nickname(member.getNickname())
                    .birth(member.getBirth())
                    .build();
        }
    }
}
