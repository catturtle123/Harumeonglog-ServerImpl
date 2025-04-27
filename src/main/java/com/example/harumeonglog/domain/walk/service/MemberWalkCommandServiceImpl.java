package com.example.harumeonglog.domain.walk.service;

import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.walk.converter.MemberWalkConverter;
import com.example.harumeonglog.domain.walk.entity.MemberWalk;
import com.example.harumeonglog.domain.walk.entity.Walk;
import com.example.harumeonglog.domain.walk.repository.MemberWalkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberWalkCommandServiceImpl implements MemberWalkCommandService {

    private final MemberWalkRepository memberWalkRepository;

    @Override
    public MemberWalk createMemberWalk(Member member, Walk walk) {
        return memberWalkRepository.save(MemberWalkConverter.toMemberWalk(member, walk));
    }
}
