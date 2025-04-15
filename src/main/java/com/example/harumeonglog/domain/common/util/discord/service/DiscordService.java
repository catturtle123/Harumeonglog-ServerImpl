package com.example.harumeonglog.domain.common.util.discord.service;

import org.springframework.web.context.request.WebRequest;

public interface DiscordService {
    void sendErrorMessage(WebRequest webRequest, Exception exception, String... message);
}
