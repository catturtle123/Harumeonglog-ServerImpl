package com.example.harumeonglog.global.discord;

import com.example.harumeonglog.global.discord.dto.DiscordMessage;


public interface DiscordApiUtil {
    
    /**
     * Discord 웹훅으로 알람 메시지 전송
     * 
     * @param discordMessage 전송할 Discord 메시지
     */
    void sendAlarm(DiscordMessage discordMessage);
}
