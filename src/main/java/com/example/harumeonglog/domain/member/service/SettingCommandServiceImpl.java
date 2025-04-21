package com.example.harumeonglog.domain.member.service;

import com.example.harumeonglog.domain.member.converter.SettingConverter;
import com.example.harumeonglog.domain.member.dto.request.SettingRequest;
import com.example.harumeonglog.domain.member.dto.response.SettingResponse;
import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.member.entity.Setting;
import com.example.harumeonglog.domain.member.repository.SettingRepository;
import com.example.harumeonglog.global.error.code.SettingErrorCode;
import com.example.harumeonglog.global.error.exception.SettingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SettingCommandServiceImpl implements SettingCommandService {

    private final SettingRepository settingRepository;

    @Override
    public SettingResponse.SettingUpdateResponse updateSetting(Member member, SettingRequest.SettingUpdateRequest request) {
        Setting setting = settingRepository.findByMember(member).orElseThrow(() ->
                new SettingException(SettingErrorCode.SETTING_NOT_FOUND));
        setting.updateSetting(request.getMorningAlarm(), request.getEventAlarm(), request.getArticleLikeAlarm(), request.getCommentAlarm());
        return SettingConverter.toSettingUpdateResponse(setting);
    }
}
