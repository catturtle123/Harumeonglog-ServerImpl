package com.example.harumeonglog.domain.member.repository;

import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.member.entity.enums.SocialType;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByProviderIdAndSocialType(String providerId, SocialType socialType);
    @Query("""
        SELECT m
        FROM Member m
        WHERE m.email LIKE %:email%
        AND m.id != :memberId
        ORDER BY m.id ASC
    """)
    Slice<Member> findByEmailContaining(@Param("email") String email,
                                        @Param("memberId")Long memberId,
                                        Pageable pageable);

    @Query("""
        SELECT m
        FROM Member m
        WHERE m.email LIKE %:email%
        AND m.id != :memberId
        AND m.id > :cursor
        ORDER BY m.id ASC
    """)
    Slice<Member> findByEmailContainingAndCursor(
            @Param("email") String email,
            @Param("cursor") Long cursor,
            @Param("memberId") Long memberId,
            Pageable pageable
    );

    @Query("SELECT m.image FROM Member m WHERE m.image IS NOT NULL")
    List<String> findAllImageKeys();

    @Modifying
    @Query("UPDATE Member m set m.deviceId = null where m = :member")
    void updateDeviceIdByMember(Member member);


    @Modifying
    @Query("UPDATE Member m SET m.image = NULL WHERE m.image IN :imageKeys")
    int updateImageKeyToNullByImageKeyIn(@Param("imageKeys") Set<String> imageKeys);
}
