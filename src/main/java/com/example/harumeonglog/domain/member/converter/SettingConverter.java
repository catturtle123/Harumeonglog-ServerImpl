package com.example.harumeonglog.domain.member.converter;

import com.example.harumeonglog.domain.member.dto.response.SettingResponse;
import com.example.harumeonglog.domain.member.entity.Setting;

public class SettingConverter {

    public static SettingResponse.SettingUpdateResponse toSettingUpdateResponse(Setting setting) {
        return SettingResponse.SettingUpdateResponse.builder()
                .memberId(setting.getMember().getId())
                .morningAlarm(setting.getMorningAlarm())
                .eventAlarm(setting.getEventAlarm())
                .articleLikeAlarm(setting.getArticleLikeAlarm())
                .commentAlarm(setting.getCommentAlarm())
                .updatedAt(setting.getUpdatedAt())
                .build();
    }

    public static SettingResponse.SettingInfoResponse toSettingInfoResponse(Setting setting) {
        return SettingResponse.SettingInfoResponse.builder()
                .memberId(setting.getMember().getId())
                .morningAlarm(setting.getMorningAlarm())
                .eventAlarm(setting.getEventAlarm())
                .articleLikeAlarm(setting.getArticleLikeAlarm())
                .commentAlarm(setting.getCommentAlarm())
                .build();
    }
}
