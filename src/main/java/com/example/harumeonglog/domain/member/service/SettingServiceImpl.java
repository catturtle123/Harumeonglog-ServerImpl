package com.example.harumeonglog.domain.member.service;

import com.example.harumeonglog.domain.member.controller.dto.request.SettingRequest;
import com.example.harumeonglog.domain.member.controller.port.SettingService;
import com.example.harumeonglog.domain.member.domain.Member;
import com.example.harumeonglog.domain.member.domain.Setting;
import org.springframework.stereotype.Service;

@Service
public class SettingServiceImpl implements SettingService {
    @Override
    public Setting updateSetting(Member member, SettingRequest.SettingUpdateRequest request) {
        return null;
    }
}
