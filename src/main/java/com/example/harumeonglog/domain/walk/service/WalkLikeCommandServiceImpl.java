package com.example.harumeonglog.domain.walk.service;

import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.walk.converter.WalkLikeConverter;
import com.example.harumeonglog.domain.walk.entity.Walk;
import com.example.harumeonglog.domain.walk.entity.WalkLike;
import com.example.harumeonglog.domain.walk.repository.WalkLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class WalkLikeCommandServiceImpl implements WalkLikeCommandService {

    private final WalkLikeRepository walkLikeRepository;

    @Override
    public WalkLike createWalkLike(Member member, Walk walk) {
        return walkLikeRepository.save(WalkLikeConverter.toWalkLike(member, walk));
    }

    @Override
    public void deleteWalkLike(WalkLike walkLike) {
        walkLikeRepository.delete(walkLike);
    }
}
