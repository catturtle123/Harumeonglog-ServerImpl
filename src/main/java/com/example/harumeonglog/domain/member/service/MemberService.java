package com.example.harumeonglog.domain.member.service;

import com.example.harumeonglog.domain.member.dto.request.MemberRequest;
import com.example.harumeonglog.domain.member.dto.response.MemberResponse;
import com.example.harumeonglog.domain.member.entity.Member;

public interface MemberService {
    MemberResponse.MemberLoginResponse login(String provider, MemberRequest.MemberLoginRequest request);
    MemberResponse.MemberLogoutResponse logout(Member member);
    Member updateInfo(Member member, MemberRequest.MemberInfoUpdateRequest request);
}
