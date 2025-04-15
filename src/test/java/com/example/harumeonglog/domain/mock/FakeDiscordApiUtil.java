package com.example.harumeonglog.domain.mock;

import com.example.harumeonglog.domain.common.config.data.DiscordConfigData;
import com.example.harumeonglog.domain.common.util.discord.DiscordApiUtil;
import com.example.harumeonglog.domain.common.util.discord.dto.DiscordMessage;

public class FakeDiscordApiUtil extends DiscordApiUtil {

    public FakeDiscordApiUtil(DiscordConfigData discordConfigData) {
        super(discordConfigData);
    }

    @Override
    public void sendAlarm(DiscordMessage discordMessage) {
    }
}
