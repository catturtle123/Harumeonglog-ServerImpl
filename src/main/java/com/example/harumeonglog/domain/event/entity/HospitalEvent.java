package com.example.harumeonglog.domain.event.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

// 진료 일정 엔티티

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "hospital_event")
public class HospitalEvent extends Event {
    @Column(name = "hospital_name")
    private String hospitalName;

    @Column(name = "department")
    private String department;

    @Column(name = "cost")
    private Integer cost;

    @Column(name = "details")
    private String details;
}
