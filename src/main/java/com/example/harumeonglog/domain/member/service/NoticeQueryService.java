package com.example.harumeonglog.domain.member.service;


import com.example.harumeonglog.domain.member.dto.response.NoticeResponse;
import com.example.harumeonglog.domain.member.entity.Member;

public interface NoticeQueryService {
    NoticeResponse.NoticeListResponse getNotices(Member member, Integer size, Long cursor);
}
