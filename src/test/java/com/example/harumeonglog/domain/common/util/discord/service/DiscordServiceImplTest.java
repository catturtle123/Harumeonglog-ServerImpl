package com.example.harumeonglog.domain.common.util.discord.service;

import com.example.harumeonglog.domain.common.config.data.DiscordConfigData;
import com.example.harumeonglog.domain.common.util.discord.DiscordApiUtil;
import com.example.harumeonglog.domain.mock.FakeDiscordApiUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

class DiscordServiceImplTest {

    private DiscordService discordService;

    @BeforeEach
    void init() {
        discordService = new DiscordServiceImpl(getDiscordApiUtil());
    }

    @Test
    @DisplayName("에러 메시지 전송")
    void sendErrorMessage() {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        Exception e = new RuntimeException("Test");
        String message = "message";

        // when
        discordService.sendErrorMessage(request, e, message);

        // then
    }

    private DiscordApiUtil getDiscordApiUtil() {
        return new FakeDiscordApiUtil(getDiscordConfigData());
    }

    private DiscordConfigData getDiscordConfigData() {
        DiscordConfigData discordConfigData = new DiscordConfigData();
        discordConfigData.setWebHookUrl("URL");
        return discordConfigData;
    }
}
