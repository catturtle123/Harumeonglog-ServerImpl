package com.example.harumeonglog.domain.member.dto.response;

import com.example.harumeonglog.domain.member.entity.Notice;
import com.example.harumeonglog.domain.member.entity.enums.NoticeType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

public class NoticeResponse {

    @Getter
    @Builder
    public static class NoticeListResponse {
        private List<NoticePreviewResponse> items;
        private Boolean hasNext;
        private Long cursor;
    }

    @Getter
    @Builder
    public static class NoticePreviewResponse {
        private Long noticeId;
        private String title;
        private String content;
        private NoticeType noticeType;
        private Long targetId;
        private String senderName;
        private LocalDateTime createdAt;
    }
}
