package com.example.harumeonglog.domain.member.service;

import com.example.harumeonglog.domain.member.dto.request.SettingRequest;
import com.example.harumeonglog.domain.member.dto.response.SettingResponse;
import com.example.harumeonglog.domain.member.entity.Member;

public interface SettingCommandService {
    SettingResponse.SettingUpdateResponse updateSetting(Member member, SettingRequest.SettingUpdateRequest request);
}
