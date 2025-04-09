package com.example.harumeonglog.domain.mock;

import com.example.harumeonglog.domain.member.entity.MemberEntity;
import com.example.harumeonglog.domain.member.service.port.MemberRepository;

import java.util.Optional;

public class FakeMemberRepository implements MemberRepository {

    @Override
    public Optional<MemberEntity> findByEmail(String email) {
        if (email.equals("success@email.com")) {
            return Optional.of(MemberEntity.builder().id(1L).email(email).build());
        }
        else {
            return Optional.empty();
        }
    }
}
