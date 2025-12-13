package com.example.harumeonglog.domain.member.service;

import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.member.entity.enums.NoticeType;

public interface NoticeCommandService {
    void deleteNotice(Long noticeId);

    void createNotice(String title, String content, NoticeType noticeType, Member sender, Member receiver);
}
