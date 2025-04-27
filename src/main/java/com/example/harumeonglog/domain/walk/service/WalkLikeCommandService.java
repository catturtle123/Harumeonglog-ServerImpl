package com.example.harumeonglog.domain.walk.service;

import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.walk.entity.Walk;
import com.example.harumeonglog.domain.walk.entity.WalkLike;

public interface WalkLikeCommandService {
    WalkLike createWalkLike(Member member, Walk walk);
    void deleteWalkLike(WalkLike walkLike);
}
