package com.example.harumeonglog.domain.walk.service;

import com.example.harumeonglog.domain.walk.entity.MemberWalk;
import com.example.harumeonglog.domain.walk.repository.MemberWalkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberWalkQueryServiceImpl implements MemberWalkQueryService {

    private final MemberWalkRepository memberWalkRepository;

    @Override
    public List<MemberWalk> findByWalk(Long walkId) {
        return memberWalkRepository.findByWalk(walkId);
    }
}
