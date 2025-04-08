package com.example.harumeonglog.domain.event.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

// 약 일정

@Getter
@SuperBuilder
public class MedicineEvent extends Event {
    private String medicineName;
    private String details;
}