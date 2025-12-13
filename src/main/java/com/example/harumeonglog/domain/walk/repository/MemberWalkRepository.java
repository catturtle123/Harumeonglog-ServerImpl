package com.example.harumeonglog.domain.walk.repository;

import com.example.harumeonglog.domain.walk.entity.MemberWalk;
import com.example.harumeonglog.domain.walk.entity.Walk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemberWalkRepository extends JpaRepository<MemberWalk, Long> {
    @Query("""
            SELECT mw FROM MemberWalk mw WHERE mw.walk.id IN :walks AND mw.id = (SELECT MIN(subMw.id) FROM MemberWalk subMw WHERE subMw.walk.id = mw.walk.id)
    """)
    List<MemberWalk> findMemberNicknameByWalks(@Param("walks") List<Long> walks);

    MemberWalk findTopByWalkOrderByCreatedAtAsc(Walk walk);

    @Query("SELECT mw FROM MemberWalk mw JOIN FETCH mw.member m WHERE mw.walk.id = :walkId")
    List<MemberWalk> findByWalk(@Param("walkId") Long walkId);
}
