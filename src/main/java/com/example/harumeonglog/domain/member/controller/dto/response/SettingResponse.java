package com.example.harumeonglog.domain.member.controller.dto.response;

import com.example.harumeonglog.domain.member.domain.Setting;
import lombok.Builder;
import lombok.Getter;

public class SettingResponse {

    @Getter
    @Builder
    public static class SettingUpdateResponse {
        private Long memberId;
        private Boolean morningAlarm;
        private Boolean eventAlarm;
        private Boolean articleLikeAlarm;
        private Boolean commentAlarm;

        public static SettingUpdateResponse from(Setting setting) {
            return SettingUpdateResponse.builder()
                    .memberId(setting.getMember().getId())
                    .morningAlarm(setting.getMorningAlarm())
                    .eventAlarm(setting.getEventAlarm())
                    .articleLikeAlarm(setting.getArticleLikeAlarm())
                    .commentAlarm(setting.getCommentAlarm())
                    .build();
        }
    }
}
