package com.example.harumeonglog.domain.member.service;

import com.example.harumeonglog.domain.member.dto.request.MemberRequest;
import com.example.harumeonglog.domain.member.entity.Member;

public interface MemberCommandService {
    Member updateInfo(Member member, MemberRequest.MemberInfoUpdateRequest request);
    void softDeleteMember(Member member);
}
