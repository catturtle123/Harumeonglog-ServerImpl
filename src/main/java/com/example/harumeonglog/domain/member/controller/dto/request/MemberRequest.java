package com.example.harumeonglog.domain.member.controller.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

public class MemberRequest {

    @Getter
    @Builder
    public static class MemberLoginRequest {
        private final String email;
        private final String nickname;
        private final LocalDate birth;
        private final String socialType;
        private final String providerId;
        private final String image;
        private final String idToken;

        public MemberLoginRequest(
                @JsonProperty("email") String email,
                @JsonProperty("nickname") String nickname,
                @JsonProperty("birth") LocalDate birth,
                @JsonProperty("socialType") String socialType,
                @JsonProperty("providerId") String providerId,
                @JsonProperty("image") String image,
                @JsonProperty("idToken") String idToken
        ) {
            this.email = email;
            this.nickname = nickname;
            this.birth = birth;
            this.socialType = socialType;
            this.providerId = providerId;
            this.image = image;
            this.idToken = idToken;
        }

    }

    @Getter
    @Builder
    public static class MemberInfoUpdateRequest {
        private final String image;
        private final String nickname;
        private final LocalDate birth;

        public MemberInfoUpdateRequest(
                @JsonProperty("image") String image,
                @JsonProperty("nickname") String nickname,
                @JsonProperty("birth") LocalDate birth
        ) {
            this.image = image;
            this.nickname = nickname;
            this.birth = birth;
        }
    }
}
