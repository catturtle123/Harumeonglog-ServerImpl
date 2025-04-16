package com.example.harumeonglog.domain.member.infrastructure;

import com.example.harumeonglog.domain.member.entity.Member;

import java.util.Optional;

public interface MemberRepository {
    Optional<Member> findByEmail(String email);
}
