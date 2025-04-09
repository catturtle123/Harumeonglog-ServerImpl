package com.example.harumeonglog.domain.event.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

// 진료 일정

@Getter
@SuperBuilder
public class HospitalEvent extends Event {
    private String hospitalName;
    private String department;
    private Integer cost;
    private String details;
}