package com.example.harumeonglog.domain.walk.repository;

import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.walk.entity.Walk;
import com.example.harumeonglog.domain.walk.entity.WalkLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface WalkLikeRepository extends JpaRepository<WalkLike, Long> {
    Optional<WalkLike> findByMemberAndWalk(Member member, Walk walk);
    boolean existsByMemberAndWalk(Member member, Walk walk);

    @Query("""
        SELECT wl FROM WalkLike wl JOIN FETCH wl.walk w WHERE wl.walk.id IN :walks AND wl.member.id = :memberId
    """)
    List<WalkLike> findByWalksAndMemberId(@Param("walks") List<Long> walks, @Param("memberId") Long memberId);
}
