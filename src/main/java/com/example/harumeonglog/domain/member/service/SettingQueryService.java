package com.example.harumeonglog.domain.member.service;

import com.example.harumeonglog.domain.member.dto.response.SettingResponse;
import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.member.entity.Setting;

public interface SettingQueryService {
    Setting getSetting(Member member);
}
