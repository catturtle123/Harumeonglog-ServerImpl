package com.example.harumeonglog.domain.member.service;

import com.example.harumeonglog.domain.member.controller.dto.request.SettingRequest;
import com.example.harumeonglog.domain.member.controller.port.SettingService;
import com.example.harumeonglog.domain.member.domain.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SettingServiceImplTest {

    private SettingService settingService;

    @BeforeEach
    void init() {
        settingService = new SettingServiceImpl();
    }

    @Test
    void canUpdateSetting() {
        //given
        Member member = Member.builder().id(1L).build();
        SettingRequest.SettingUpdateRequest settingUpdateRequest = new SettingRequest.SettingUpdateRequest(true, true, true, true);

        //when
        settingService.updateSetting(member, settingUpdateRequest);

        //then
    }
}