package com.example.harumeonglog.domain.member.service;

import com.example.harumeonglog.domain.member.dto.request.MemberBlockRequest;
import com.example.harumeonglog.domain.member.dto.response.MemberBlockResponse;
import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.member.entity.MemberBlock;

public interface MemberBlockCommandService {
    MemberBlockResponse.MemberBlockInfoResponse updateBlock(Member member, MemberBlockRequest.UpdateMemberBlockRequest request);
}
