package com.example.harumeonglog.domain.event.repository;

import com.example.harumeonglog.domain.event.entity.Event;
import com.example.harumeonglog.domain.event.entity.enums.EventCategory;
import com.example.harumeonglog.domain.member.entity.Member;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;

public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("SELECT e FROM Event e " +
            "WHERE e.date = :date " +
            "AND e.member = :member " +
            "AND e.deletedAt IS NULL " +
            "AND (:cursor IS NULL OR e.id > :cursor) " +
            "ORDER BY e.createdAt ASC")
    Slice<Event> findByDateAndMemberAndDeletedAtIsNull(
            @Param("date") LocalDate date,
            @Param("member") Member member,
            @Param("cursor") Long cursor,
            Pageable pageable);

    @Query("SELECT e FROM Event e " +
            "WHERE e.date = :date " +
            "AND e.category = :category " +
            "AND e.member = :member " +
            "AND e.deletedAt IS NULL " +
            "AND (:cursor IS NULL OR e.id > :cursor) " +
            "ORDER BY e.createdAt ASC")
    Slice<Event> findByDateAndCategoryAndMemberAndDeletedAtIsNull(
            @Param("date") LocalDate date,
            @Param("category") EventCategory category,
            @Param("member") Member member,
            @Param("cursor") Long cursor,
            Pageable pageable);
}
