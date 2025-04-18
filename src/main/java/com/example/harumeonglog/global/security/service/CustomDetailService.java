package com.example.harumeonglog.global.security.service;

import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.global.security.domain.CustomUserDetails;
import com.example.harumeonglog.global.error.code.MemberErrorCode;
import com.example.harumeonglog.global.error.exception.MemberException;
import com.example.harumeonglog.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomDetailService {

    private final MemberRepository memberRepository;

    public CustomUserDetails loadUserById(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND));
        return new CustomUserDetails(member);
    }
}
