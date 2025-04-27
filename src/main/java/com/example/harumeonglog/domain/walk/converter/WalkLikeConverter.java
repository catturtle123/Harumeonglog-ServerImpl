package com.example.harumeonglog.domain.walk.converter;

import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.walk.entity.Walk;
import com.example.harumeonglog.domain.walk.entity.WalkLike;

public class WalkLikeConverter {
    public static WalkLike toWalkLike(Member member, Walk walk) {
        return WalkLike.builder()
                .member(member)
                .walk(walk)
                .build();
    }
}
