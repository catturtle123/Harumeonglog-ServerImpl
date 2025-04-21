package com.example.harumeonglog.domain.member.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

public class MemberRequest {

    @Getter
    @Builder
    public static class MemberInfoUpdateRequest {
        private final String image;
        private final String nickname;

        public MemberInfoUpdateRequest(
                @JsonProperty("image") String image,
                @JsonProperty("nickname") String nickname
        ) {
            this.image = image;
            this.nickname = nickname;
        }
    }
}
