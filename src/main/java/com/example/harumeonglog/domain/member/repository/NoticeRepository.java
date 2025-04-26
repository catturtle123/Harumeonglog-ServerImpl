package com.example.harumeonglog.domain.member.repository;

import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.member.entity.Notice;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

    Slice<Notice> findByMemberAndIdLessThanOrderByIdDesc(Member member, Long Id, Pageable pageable);

    @Query("select count(n) from Notice n where n.member = :member")
    Integer countAlarm(Member member);
}
