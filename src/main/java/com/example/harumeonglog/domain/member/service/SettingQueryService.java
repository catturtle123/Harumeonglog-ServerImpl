package com.example.harumeonglog.domain.member.service;

import com.example.harumeonglog.domain.member.dto.response.SettingResponse;
import com.example.harumeonglog.domain.member.entity.Member;

public interface SettingQueryService {
    SettingResponse.SettingInfoResponse getSetting(Member member);
}
