package com.example.harumeonglog.domain.member.service;

import com.example.harumeonglog.domain.member.dto.request.SettingRequest;
import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.member.entity.Setting;
import lombok.Builder;
import org.springframework.stereotype.Service;

@Service
@Builder
public class SettingServiceImpl implements SettingService {
    @Override
    public Setting updateSetting(Member member, SettingRequest.SettingUpdateRequest request) {
        return null;
    }
}
