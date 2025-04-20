package com.example.harumeonglog.domain.member.dto.response;

import com.example.harumeonglog.domain.member.entity.Setting;
import lombok.Builder;
import lombok.Getter;

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
    }
}
