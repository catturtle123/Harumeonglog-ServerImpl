package com.example.harumeonglog.domain.member.service;

import com.example.harumeonglog.domain.member.converter.NoticeConverter;
import com.example.harumeonglog.domain.member.dto.response.NoticeResponse;
import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.member.entity.Notice;
import com.example.harumeonglog.domain.member.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoticeQueryServiceImpl implements NoticeQueryService {

    private final NoticeRepository noticeRepository;

    @Override
    public NoticeResponse.NoticeListResponse getNotices(Member member, Integer size, Long cursor) {

        if (cursor == 0) {
            cursor = Long.MAX_VALUE;
        }

        Slice<Notice> noticeSlice = noticeRepository.findByMemberAndIdLessThanOrderByIdDesc(member, cursor, PageRequest.of(0, size));

        List<Notice> noticeList = noticeSlice.toList();

        Long nextCursor = null;
        if (!noticeList.isEmpty() && noticeSlice.hasNext()) {
            nextCursor = noticeList.get(noticeList.size() - 1).getId();
        }

        List<NoticeResponse.NoticePreviewResponse> noticePreviewResponseList = noticeList.stream().map(NoticeConverter::toNoticePreviewResponse).toList();

        return NoticeConverter.toNoticeListResponse(noticePreviewResponseList, noticeSlice.hasNext(), nextCursor);
    }

}
