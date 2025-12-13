package com.example.harumeonglog.domain.member.service;

import com.example.harumeonglog.domain.member.dto.response.MemberBlockResponse;

public interface MemberBlockQueryService {
    MemberBlockResponse.MemberBlockInfoResponse isBlock(Long reporterId, Long reportedId);
}
