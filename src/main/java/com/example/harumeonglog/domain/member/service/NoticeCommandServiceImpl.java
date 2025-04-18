package com.example.harumeonglog.domain.member.service;

import com.example.harumeonglog.domain.member.entity.Notice;
import com.example.harumeonglog.domain.member.repository.NoticeRepository;
import com.example.harumeonglog.global.error.code.NoticeErrorCode;
import com.example.harumeonglog.global.error.exception.NoticeException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class NoticeCommandServiceImpl implements NoticeCommandService {

    private final NoticeRepository noticeRepository;

    @Override
    public void deleteNotice(Long noticeId) {
        Notice notice = noticeRepository.findById(noticeId).orElseThrow(() -> new NoticeException(NoticeErrorCode.NOT_FOUND));

        noticeRepository.delete(notice);
    }
}
