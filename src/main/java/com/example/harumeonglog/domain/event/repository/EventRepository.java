package com.example.harumeonglog.domain.event.repository;

import com.example.harumeonglog.domain.event.entity.Event;
import com.example.harumeonglog.domain.event.entity.enums.EventCategory;
import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.pet.entity.Pet;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

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

    void deleteByOriginalEventId(Long originalEventId);

    @Query("SELECT DISTINCT e.date FROM Event e " +
            "WHERE e.pet = :pet " +
            "AND e.date BETWEEN :startDate AND :endDate " +
            "AND e.deletedAt IS NULL " +
            "ORDER BY e.date")
    List<LocalDate> findDistinctDatesByMemberAndDateBetweenAndDeletedAtIsNull(
            @Param("member") Pet pet,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
}
