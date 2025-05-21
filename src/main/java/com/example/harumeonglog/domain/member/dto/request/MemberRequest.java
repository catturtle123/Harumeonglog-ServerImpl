package com.example.harumeonglog.domain.member.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

public class MemberRequest {

    @Getter
    @Builder
    public static class MemberInfoUpdateRequest {
        private final String imageKey;
        private final String nickname;

        public MemberInfoUpdateRequest(
                @JsonProperty("imageKey") String imageKey,
                @JsonProperty("nickname") String nickname
        ) {
            this.imageKey = imageKey;
            this.nickname = nickname;
        }
    }

    @Getter
    @Builder
    public static class FCMRequest {
        private String fcmToken;
    }
}
