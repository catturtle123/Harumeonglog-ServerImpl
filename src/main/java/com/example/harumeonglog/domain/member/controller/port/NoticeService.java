package com.example.harumeonglog.domain.member.controller.port;

import com.example.harumeonglog.domain.member.domain.Notice;
import org.springframework.data.domain.Slice;

public interface NoticeService {
    Slice<Notice> getNotices(Integer size, Long cursor);

    void deleteNotice(Long noticeId);
}
