package com.example.harumeonglog.domain.event.domain;

import com.example.harumeonglog.domain.event.domain.enums.EventCategory;
import com.example.harumeonglog.domain.member.entity.MemberEntity;
import com.example.harumeonglog.domain.pet.domain.Pet;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class Event {

    private Long id;

    private LocalDate date;

    private String title;

    private Boolean isRepeated;

    private LocalDate expiredDate;

    private Boolean hasNotice;

    private EventCategory category;

    private String details;

    private LocalDateTime deletedAt;

    private MemberEntity memberEntity;

    private Pet pet;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
