package com.example.harumeonglog.domain.event.dto.request;

import com.example.harumeonglog.domain.event.entity.enums.EventCategory;
import com.example.harumeonglog.domain.event.entity.enums.RepeatDay;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class EventRequest {

    @Getter
    public static class EventRequestDTO {
        private String title;
        private LocalDate date;
        private Boolean isRepeated;
        private LocalDate expiredDate;
        private List<RepeatDay> repeatDays;
        private Boolean hasNotice;
        private LocalTime time;
        private EventCategory category;
        // 카테고리별 필드 (선택적)
        private String details;        // GENERAL, HOSPITAL, MEDICINE, WALK
        private String hospitalName;   // HOSPITAL
        private String department;     // HOSPITAL
        private Integer cost;          // HOSPITAL
        private String medicineName;   // MEDICINE
        private String distance;       // WALK
        private String duration;       // WALK
    }
}