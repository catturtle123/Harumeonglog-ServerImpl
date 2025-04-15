package com.example.harumeonglog.domain.common.util.discord.service;

import com.example.harumeonglog.domain.common.util.discord.DiscordApiUtil;
import com.example.harumeonglog.domain.common.util.discord.dto.DiscordMessage;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DiscordServiceImpl implements DiscordService {

    private static final String DISCORD_CONTENT = "# 🚨 에러 발생 비이이이이사아아아앙";
    private static final String DISCORD_TITLE = "ℹ️ 에러 정보";
    private static final String DISCORD_DESCRIPTION_FORMAT = "### 🕖 발생 시간\n%s\n### 🔗 요청 URL\n%s\n### 📝 Method & Class\n```\n%s\n```";
    private static final String DISCORD_STACK_TRACE_TITLE = "📚 Stack Trace";
    private static final String DISCORD_STACK_TRACE_DESCRIPTION_FORMAT = "```\n%s\n```";

    private final DiscordApiUtil discordApiUtil;

    @Override
    public void sendErrorMessage(WebRequest webRequest, Exception exception, String... message) {
        DiscordMessage discordMessage = createDiscordMessage(webRequest, exception, String.join("\n", message));
        discordApiUtil.sendAlarm(discordMessage);
    }

    private DiscordMessage createDiscordMessage(WebRequest webRequest, Exception exception, String message) {
        String description = String.format(DISCORD_DESCRIPTION_FORMAT, LocalDateTime.now(), createRequestFullPath(webRequest), message);
        return DiscordMessage.from(DISCORD_CONTENT, DISCORD_TITLE, description, DISCORD_STACK_TRACE_TITLE, String.format(DISCORD_STACK_TRACE_DESCRIPTION_FORMAT, getStackTrace(exception).substring(0, 1000)));
    }

    private String createRequestFullPath(WebRequest webRequest) {
        HttpServletRequest request = ((ServletWebRequest) webRequest).getRequest();
        String fullPath = request.getMethod() + " " + request.getRequestURL();

        String queryString = request.getQueryString();
        if (queryString != null) {
            fullPath += "?" + queryString;
        }

        return fullPath;
    }

    private String getStackTrace(Exception e) {
        StringWriter stringWriter = new StringWriter();
        e.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }
}
