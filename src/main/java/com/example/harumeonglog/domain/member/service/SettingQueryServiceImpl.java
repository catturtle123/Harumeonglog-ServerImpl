package com.example.harumeonglog.domain.member.service;

import com.example.harumeonglog.domain.member.converter.SettingConverter;
import com.example.harumeonglog.domain.member.dto.response.SettingResponse;
import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.member.repository.SettingRepository;
import com.example.harumeonglog.global.error.code.SettingErrorCode;
import com.example.harumeonglog.global.error.exception.SettingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SettingQueryServiceImpl implements SettingQueryService {

    private final SettingRepository settingRepository;

    @Override
    public SettingResponse.SettingInfoResponse getSetting(Member member) {
        return SettingConverter.toSettingInfoResponse(settingRepository.findByMember(member).orElseThrow(() ->
                new SettingException(SettingErrorCode.SETTING_NOT_FOUND)));
    }
}
