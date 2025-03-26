package com.example.harumeonglog.domain.member.service;

import com.example.harumeonglog.domain.member.controller.dto.request.MemberRequest;
import com.example.harumeonglog.domain.member.controller.dto.response.MemberResponse;
import com.example.harumeonglog.domain.member.controller.port.MemberService;
import com.example.harumeonglog.domain.member.domain.Member;
import org.springframework.stereotype.Service;

@Service
public class MemberServiceImpl implements MemberService {

    @Override
    public MemberResponse.MemberLoginResponse login(MemberRequest.MemberLoginRequest request) {
        return null;
    }

    @Override
    public MemberResponse.MemberLogoutResponse logout(Member member) {
        return null;
    }

    @Override
    public Member updateInfo(Member member, MemberRequest.MemberInfoUpdateRequest request) {
        return null;
    }
}
