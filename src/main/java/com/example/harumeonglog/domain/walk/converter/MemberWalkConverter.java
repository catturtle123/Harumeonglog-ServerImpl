package com.example.harumeonglog.domain.walk.converter;

import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.walk.entity.MemberWalk;
import com.example.harumeonglog.domain.walk.entity.Walk;

public class MemberWalkConverter {
    public static MemberWalk toMemberWalk(Member member, Walk walk) {
        return MemberWalk.builder()
                .member(member)
                .walk(walk)
                .build();
    }
}
