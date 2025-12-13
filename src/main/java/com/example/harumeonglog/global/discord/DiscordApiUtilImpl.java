package com.example.harumeonglog.global.discord;

import com.example.harumeonglog.global.data.DiscordConfigData;
import com.example.harumeonglog.global.discord.dto.DiscordMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
@Slf4j
@Primary
public class DiscordApiUtilImpl implements DiscordApiUtil {

    private final RestTemplate discordRestTemplate;
    private final ObjectMapper objectMapper;
    private final DiscordConfigData discordConfigData;

    @Override
    public void sendAlarm(DiscordMessage discordMessage) {
        try {
            String jsonMessage = objectMapper.writeValueAsString(discordMessage);

            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");

            HttpEntity<String> request = new HttpEntity<>(jsonMessage, headers);

            ResponseEntity<String> response = discordRestTemplate.exchange(
                    discordConfigData.getWebHookUrl(),
                    HttpMethod.POST,
                    request,
                    String.class
            );

            if (response.getStatusCode() != HttpStatus.NO_CONTENT) {
                log.warn("Discord send failed: {}", response.getStatusCode());
            }
        } catch (JsonProcessingException e) {
            log.warn("Discord send error", e);
        }
    }
}
