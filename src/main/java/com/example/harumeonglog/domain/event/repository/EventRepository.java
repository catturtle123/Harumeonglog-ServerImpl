package com.example.harumeonglog.domain.event.repository;

import com.example.harumeonglog.domain.event.entity.Event;
import com.example.harumeonglog.domain.event.entity.enums.EventCategory;
import com.example.harumeonglog.domain.pet.entity.Pet;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("SELECT e FROM Event e " +
            "WHERE e.date = :date " +
            "AND e.pet = :pet " +
            "AND e.deletedAt IS NULL " +
            "AND (:cursor IS NULL OR e.id > :cursor) " +
            "ORDER BY e.createdAt ASC")
    Slice<Event> findByDateAndPetAndDeletedAtIsNull(
            @Param("date") LocalDate date,
            @Param("pet") Pet pet,
            @Param("cursor") Long cursor,
            Pageable pageable);

    @Query("SELECT e FROM Event e " +
            "WHERE e.date = :date " +
            "AND e.category = :category " +
            "AND e.pet = :pet " +
            "AND e.deletedAt IS NULL " +
            "AND (:cursor IS NULL OR e.id > :cursor) " +
            "ORDER BY e.createdAt ASC")
    Slice<Event> findByDateAndCategoryAndPetAndDeletedAtIsNull(
            @Param("date") LocalDate date,
            @Param("category") EventCategory category,
            @Param("pet") Pet pet,
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


    List<Event> findByDateAndDeletedAtIsNull(LocalDate date);
    @Query("SELECT e FROM Event e " +
            "WHERE e.date = :date " +
            "AND e.time BETWEEN :startTime " +
            "AND :endTime AND e.hasNotice = true " +
            "AND e.isNoticed = false AND e.deletedAt IS NULL")
    List<Event> findByDateAndTimeBetweenAndHasNoticeTrueAndIsNoticedFalseAndDeletedAtIsNull(
            @Param("date") LocalDate date,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime
    );
}
