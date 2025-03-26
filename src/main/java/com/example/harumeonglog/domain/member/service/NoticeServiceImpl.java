package com.example.harumeonglog.domain.member.service;

import com.example.harumeonglog.domain.member.controller.port.NoticeService;
import com.example.harumeonglog.domain.member.domain.Notice;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@Service
public class NoticeServiceImpl implements NoticeService {
    @Override
    public Slice<Notice> getNotices(Integer size, Long cursor) {
        return null;
    }

    @Override
    public void deleteNotice(Long noticeId) {

    }

}
