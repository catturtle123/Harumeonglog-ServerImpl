package com.example.harumeonglog.global.discord;

import com.example.harumeonglog.global.discord.dto.DiscordMessage;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * 테스트용 Fake Discord API 구현체
 * 실제 Discord 웹훅 호출 없이 메시지 전송을 시뮬레이션
 */
@Slf4j
public class FakeDiscordApiUtilImpl implements DiscordApiUtil {

    private final List<DiscordMessage> sentMessages = new ArrayList<>();
    private boolean shouldFail = false;

    @Override
    public void sendAlarm(DiscordMessage discordMessage) {
        if (shouldFail) {
            throw new RuntimeException("Fake Discord API 전송 실패");
        }
        
        sentMessages.add(discordMessage);
        log.info("Fake Discord API: 메시지 전송됨 - content: {}", discordMessage.getContent());
    }

    /**
     * 전송된 메시지 목록 조회
     */
    public List<DiscordMessage> getSentMessages() {
        return new ArrayList<>(sentMessages);
    }

    /**
     * 전송된 메시지 개수 조회
     */
    public int getSentMessageCount() {
        return sentMessages.size();
    }

    /**
     * 마지막으로 전송된 메시지 조회
     */
    public DiscordMessage getLastSentMessage() {
        if (sentMessages.isEmpty()) {
            return null;
        }
        return sentMessages.get(sentMessages.size() - 1);
    }

    /**
     * 전송된 메시지 초기화
     */
    public void clear() {
        sentMessages.clear();
    }

    /**
     * 전송 실패 시뮬레이션 설정
     */
    public void setShouldFail(boolean shouldFail) {
        this.shouldFail = shouldFail;
    }
}
