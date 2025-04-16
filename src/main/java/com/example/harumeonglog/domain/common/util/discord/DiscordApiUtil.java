package com.example.harumeonglog.domain.common.util.discord;

import com.example.harumeonglog.domain.common.config.data.DiscordConfigData;
import com.example.harumeonglog.domain.common.util.discord.dto.DiscordMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class DiscordApiUtil {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final DiscordConfigData discordConfigData;

    public void sendAlarm(DiscordMessage discordMessage) {
        try {
            String jsonMessage = objectMapper.writeValueAsString(discordMessage);

            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");

            HttpEntity<String> request = new HttpEntity<>(jsonMessage, headers);

            ResponseEntity<String> response = restTemplate.exchange(discordConfigData.getWebHookUrl(), HttpMethod.POST, request, String.class);

            if (response.getStatusCode() != HttpStatus.NO_CONTENT) {
                throw new RuntimeException("Failed to send Discord message: " + response.getStatusCode());
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to send Discord message", e);
        }
    }
}
