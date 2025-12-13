package com.example.harumeonglog.domain.member.converter;

import com.example.harumeonglog.domain.member.dto.response.NoticeResponse;
import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.member.entity.Notice;
import com.example.harumeonglog.domain.member.entity.enums.NoticeType;

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
                .title(notice.getTitle())
                .content(notice.getContent())
                .createdAt(notice.getCreatedAt())
                .senderName(notice.getSenderName())
                .noticeType(notice.getNoticeType())
                .build();
    }

    public static Notice toNotice(String title, String content, NoticeType noticeType, Member sender, Member receiver) {
        return Notice.builder()
                .title(title)
                .content(content)
                .noticeType(noticeType)
                .senderId(sender.getDeviceId())
                .senderName(sender.getNickname())
                .member(receiver)
                .build();
    }
}
