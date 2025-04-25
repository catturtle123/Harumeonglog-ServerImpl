package com.example.harumeonglog.domain.pet.repository;

import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.pet.entity.MemberPet;
import com.example.harumeonglog.domain.pet.entity.Pet;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MemberPetRepository extends JpaRepository<MemberPet, Long> {
    Optional<MemberPet> findByMemberAndPet(Member member, Pet pet);

    @Query("""
        SELECT mp
        FROM MemberPet mp
        LEFT JOIN FETCH mp.pet p
        LEFT JOIN FETCH mp.member m
        WHERE mp.member.id = :memberId
        AND p.deletedAt IS NULL
        ORDER BY p.id ASC
    """)
    Slice<MemberPet> findFirstPageByMemberId(@Param("memberId") Long memberId, Pageable pageable);

    @Query("""
        SELECT mp
        FROM MemberPet mp
        LEFT JOIN FETCH mp.pet p
        LEFT JOIN FETCH mp.member m
        WHERE mp.member.id = :memberId
        AND p.deletedAt IS NULL
        AND p.id > :cursor
        ORDER BY p.id ASC
    """)
    Slice<MemberPet> findByMemberAndCursor(
            @Param("memberId") Long memberId,
            @Param("cursor") Long cursor,
            Pageable pageable);

    @Query("""
        SELECT mp
        FROM MemberPet mp
        LEFT JOIN FETCH mp.pet p
        WHERE mp.member.id = :memberId
        AND p.deletedAt IS NULL
        ORDER BY p.id ASC
    """)
    List<MemberPet> findAllByMemberId(@Param("memberId") Long memberId);

    @Query("SELECT mp FROM MemberPet mp LEFT JOIN FETCH mp.member WHERE mp.pet.id IN :petIds AND mp.member.id != :memberId")
    List<MemberPet> findByPetIdsAndNotMemberId(@Param("petIds") List<Long> petIds, @Param("memberId") Long memberId);

    void deleteByPet(Pet pet);
}
