package com.example.harumeonglog.domain.member.service;

import com.example.harumeonglog.domain.member.dto.request.SettingRequest;
import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.member.entity.Setting;

public interface SettingCommandService {
    Setting updateSetting(Member member, SettingRequest.SettingUpdateRequest request);
}
