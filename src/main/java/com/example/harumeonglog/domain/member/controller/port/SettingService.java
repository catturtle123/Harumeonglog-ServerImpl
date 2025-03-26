package com.example.harumeonglog.domain.member.controller.port;

import com.example.harumeonglog.domain.member.controller.dto.request.SettingRequest;
import com.example.harumeonglog.domain.member.domain.Member;
import com.example.harumeonglog.domain.member.domain.Setting;

public interface SettingService {
    Setting updateSetting(Member member, SettingRequest.SettingUpdateRequest request);
}
