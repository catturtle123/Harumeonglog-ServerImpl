package com.example.harumeonglog.domain.member.controller.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

public class SettingRequest {

    @Getter
    public static class SettingUpdateRequest {
        private final Boolean morningAlarm;
        private final Boolean eventAlarm;
        private final Boolean articleLikeAlarm;
        private final Boolean commentAlarm;

        public SettingUpdateRequest(
                @JsonProperty("morningAlarm") Boolean morningAlarm,
                @JsonProperty("eventAlarm") Boolean eventAlarm,
                @JsonProperty("articleLikeAlarm") Boolean articleLikeAlarm,
                @JsonProperty("commentAlarm") Boolean commentAlarm
        ) {
            this.morningAlarm = morningAlarm;
            this.eventAlarm = eventAlarm;
            this.articleLikeAlarm = articleLikeAlarm;
            this.commentAlarm = commentAlarm;
        }
    }
}
