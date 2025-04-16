package com.example.harumeonglog.domain.common.util.discord.dto;

import lombok.*;

import java.util.List;

@Builder
@Getter
public class DiscordMessage {

    private String content;
    private List<Embed> embeds;

    public static DiscordMessage from(String content, String title1, String description1, String title2, String description2) {
        return from(content,
                List.of(
                        Embed.from(title1, description1),
                        Embed.from(title2, description2)
                )
        );
    }

    public static DiscordMessage from(String content, List<Embed> embeds) {
        return DiscordMessage.builder()
                .content(content)
                .embeds(embeds)
                .build();
    }

    @Builder
    @Getter
    public static class Embed {

        private String title;
        private String description;

        public static Embed from(String title, String description) {
            return Embed.builder()
                    .title(title)
                    .description(description)
                    .build();
        }
    }
}