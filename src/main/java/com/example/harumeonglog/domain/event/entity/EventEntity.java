package com.example.harumeonglog.domain.event.entity;

import com.example.harumeonglog.domain.common.entity.BaseEntity;
import com.example.harumeonglog.domain.event.domain.enums.EventCategory;
import com.example.harumeonglog.domain.member.entity.MemberEntity;
import com.example.harumeonglog.domain.pet.entity.PetEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "event")
public class EventEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long id;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "is_repeated", nullable = false)
    private Boolean isRepeated;

    @Column(name = "expired_date")
    private LocalDate expiredDate;

    @Column(name = "has_notice", nullable = false)
    private Boolean hasNotice;

    @Column(name = "done", nullable = false)
    private Boolean done;

    @Column(name = "category", nullable = false)
    @Enumerated(EnumType.STRING)
    private EventCategory category;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private MemberEntity memberEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id", nullable = false)
    private PetEntity petEntity;
}
