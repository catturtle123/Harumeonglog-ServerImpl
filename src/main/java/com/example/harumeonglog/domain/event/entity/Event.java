package com.example.harumeonglog.domain.event.entity;

import com.example.harumeonglog.domain.event.entity.enums.EventCategory;
import com.example.harumeonglog.domain.event.entity.enums.RepeatDay;
import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.pet.entity.Pet;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@SuperBuilder
@EntityListeners(AuditingEntityListener.class)
@Table(name = "event")
public class Event{

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

    @ElementCollection
    @CollectionTable(name = "event_repeat_days", joinColumns = @JoinColumn(name = "event_id"))
    @Column(name = "repeat_day")
    @Enumerated(EnumType.STRING)
    private List<RepeatDay> repeatDays;

    @Column(name = "is_original_event")
    private Boolean isOriginalEvent;

    @Column(name = "original_event_id")
    private Long originalEventId;

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
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id", nullable = false)
    private Pet pet;

    @CreatedDate
    @Column(name = "createdAt", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updatedAt")
    private LocalDateTime updatedAt;
}
