package com.example.harumeonglog.domain.member.repository;

import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.member.entity.MemberBlock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface MemberBlockRepository extends JpaRepository<MemberBlock, Long> {

    @Query("SELECT mb FROM MemberBlock mb WHERE mb.reporter.id = :reporterId AND mb.reported.id = :reportedId")
    Optional<MemberBlock> findByReporterAndReported(@Param("reporterId") Long reporterId, @Param("reportedId") Long reportedId);

    @Query("SELECT COUNT(mb) > 0 FROM MemberBlock mb WHERE mb.reporter.id = :reporterId AND mb.reported.id = :reportedId")
    boolean existsByReporterAndReported(@Param("reporterId") Long reporterId, @Param("reportedId") Long reportedId);
}
