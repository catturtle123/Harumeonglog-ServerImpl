package com.example.harumeonglog.domain.member.controller.dto.response;

import com.example.harumeonglog.domain.member.domain.Notice;
import com.example.harumeonglog.domain.member.domain.enums.NoticeType;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class NoticeResponse {

    @Getter
    @Builder
    public static class NoticeListResponse {
        private List<NoticePreviewResponse> items;
        private Boolean hasNext;
        private Long cursor;


        public static NoticeListResponse from(Long cursor, Boolean hasNext, List<Notice> items) {
            return NoticeListResponse.builder()
                    .items(items.stream().map(NoticePreviewResponse::from).toList())
                    .cursor(cursor)
                    .hasNext(hasNext)
                    .build();
        }
    }

    @Getter
    @Builder
    public static class NoticePreviewResponse {
        private Long noticeId;
        private String title;
        private String content;
        private NoticeType noticeType;
        private Long targetId;

        public static NoticePreviewResponse from(Notice notice) {
            return NoticePreviewResponse.builder()
                    .noticeId(notice.getId())
                    .content(notice.getContent())
                    .title(notice.getTitle())
                    .noticeType(notice.getNoticeType())
                    .targetId(notice.getTargetId())
                    .build();
        }
    }
}
