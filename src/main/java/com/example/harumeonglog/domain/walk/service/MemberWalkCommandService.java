package com.example.harumeonglog.domain.walk.service;

import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.walk.entity.MemberWalk;
import com.example.harumeonglog.domain.walk.entity.Walk;

public interface MemberWalkCommandService {
    MemberWalk createMemberWalk(Member member, Walk walk);
}
