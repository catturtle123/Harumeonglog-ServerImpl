package com.example.harumeonglog.domain.event.domain;

import com.example.harumeonglog.domain.event.domain.enums.EventCategory;
import com.example.harumeonglog.domain.member.domain.Member;
import com.example.harumeonglog.domain.member.entity.MemberEntity;
import com.example.harumeonglog.domain.pet.domain.Pet;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@SuperBuilder
public class Event {

    private Long id;

    private LocalDate date;

    private String title;

    private Boolean isRepeated;

    private LocalDate expiredDate;

    private Boolean hasNotice;

    private Boolean done;

    private EventCategory category;

    private LocalDateTime deletedAt;

    private Member member;

    private Pet pet;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
