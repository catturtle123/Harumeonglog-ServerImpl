package com.example.harumeonglog.domain.member.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

public class MemberRequest {

    @Getter
    @Builder
    public static class MemberLoginRequest {
        private final String idToken;

        public MemberLoginRequest(
                @JsonProperty("idToken") String idToken
        ) {
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
