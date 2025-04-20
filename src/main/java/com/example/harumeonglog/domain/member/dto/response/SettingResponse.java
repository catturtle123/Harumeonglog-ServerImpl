package com.example.harumeonglog.domain.member.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

public class SettingResponse {

    @Getter
    @Builder
    public static class SettingInfoResponse {
        private Long memberId;
        private Boolean morningAlarm;
        private Boolean eventAlarm;
        private Boolean articleLikeAlarm;
        private Boolean commentAlarm;
    }

    @Getter
    @Builder
    public static class SettingUpdateResponse {
        private Long memberId;
        private Boolean morningAlarm;
        private Boolean eventAlarm;
        private Boolean articleLikeAlarm;
        private Boolean commentAlarm;
        private LocalDateTime updatedAt;
    }
}
