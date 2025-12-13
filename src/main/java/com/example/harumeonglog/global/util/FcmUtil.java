package com.example.harumeonglog.global.util;

import com.example.harumeonglog.domain.member.entity.enums.NoticeType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


@Component
@RequiredArgsConstructor
@Slf4j
public class FcmUtil {

    private final ObjectMapper objectMapper;

    public String createFcmPayload(Long memberId, String title, String body, NoticeType noticeType, Long domainId) {
        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put("memberId", memberId);
            payload.put("title", title);
            payload.put("body", body);
            payload.put("noticeType", noticeType.toString());

            if (domainId != null) {
                payload.put("domainId", domainId);
            }

            return objectMapper.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            log.error("FCM 페이로드 생성 중 오류가 발생했습니다: {}", e.getMessage(), e);
            return String.format("{\"memberId\": %d, \"title\": \"%s\", \"body\": \"%s\", \"noticeType\": \"%s\"}",
                    memberId, title, body, noticeType);
        }
    }
}
