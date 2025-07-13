package com.example.harumeonglog.domain.member.service;

import com.example.harumeonglog.domain.member.converter.NoticeConverter;
import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.member.entity.Notice;
import com.example.harumeonglog.domain.member.entity.enums.NoticeType;
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

    @Override
    public void createNotice(String title, String content, NoticeType noticeType, Member sender, Member receiver) {
        Notice notice = NoticeConverter.toNotice(title, content, noticeType, sender, receiver);

        noticeRepository.save(notice);
    }
}
