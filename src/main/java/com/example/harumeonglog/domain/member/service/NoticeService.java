package com.example.harumeonglog.domain.member.service;


import com.example.harumeonglog.domain.member.entity.Notice;
import org.springframework.data.domain.Slice;

public interface NoticeService {
    Slice<Notice> getNotices(Integer size, Long cursor);

    void deleteNotice(Long noticeId);
}
