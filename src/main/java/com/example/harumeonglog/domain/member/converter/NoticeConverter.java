package com.example.harumeonglog.domain.member.converter;

import com.example.harumeonglog.domain.member.dto.response.NoticeResponse;
import com.example.harumeonglog.domain.member.entity.Notice;

import java.util.List;

public class NoticeConverter {

    public static NoticeResponse.NoticeListResponse toNoticeListResponse(List<NoticeResponse.NoticePreviewResponse> noticePreviewResponseList, Boolean hasNext, Long cursor) {
        return NoticeResponse.NoticeListResponse.builder()
                .items(noticePreviewResponseList)
                .hasNext(hasNext)
                .cursor(cursor)
                .build();
    }

    public static NoticeResponse.NoticePreviewResponse toNoticePreviewResponse(Notice notice) {
        return NoticeResponse.NoticePreviewResponse.builder()
                .noticeId(notice.getId())
                .noticeType(notice.getNoticeType())
                .title(notice.getTitle())
                .content(notice.getContent())
                .targetId(notice.getTargetId())
                .createdAt(notice.getCreatedAt())
                .senderName(notice.getSenderName())
                .build();
    }
}
