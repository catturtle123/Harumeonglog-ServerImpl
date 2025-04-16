package com.example.harumeonglog.domain.member.infrastructure;

import com.example.harumeonglog.domain.member.entity.Member;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class MemberRepositoryImpl implements MemberRepository {

    @Override
    public Optional<Member> findByEmail(String email) {
        return null;
    }
}
