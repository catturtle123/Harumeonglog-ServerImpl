package com.example.harumeonglog.domain.walk.repository;

import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.walk.entity.Walk;
import com.example.harumeonglog.domain.walk.entity.WalkLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WalkLikeRepository extends JpaRepository<WalkLike, Long> {
    Optional<WalkLike> findByMemberAndWalk(Member member, Walk walk);
}
