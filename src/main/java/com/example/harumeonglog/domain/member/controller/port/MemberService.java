package com.example.harumeonglog.domain.member.controller.port;

import com.example.harumeonglog.domain.member.controller.dto.request.MemberRequest;
import com.example.harumeonglog.domain.member.controller.dto.response.MemberResponse;
import com.example.harumeonglog.domain.member.domain.Member;

public interface MemberService {
    MemberResponse.MemberLoginResponse login(MemberRequest.MemberLoginRequest request);
    MemberResponse.MemberLogoutResponse logout(Member member);
    Member updateInfo(Member member, MemberRequest.MemberInfoUpdateRequest request);
}
