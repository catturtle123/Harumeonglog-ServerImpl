package com.example.harumeonglog.domain.event.dto.request;

import com.example.harumeonglog.domain.event.entity.enums.EventCategory;
import com.example.harumeonglog.domain.event.entity.enums.RepeatDay;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

public class EventRequest {

    @Getter
    @Setter
    public static class EventCreateRequest {
        private String title;
        private LocalDate date;
        private Boolean isRepeated;
        private LocalDate expiredDate;
        private List<RepeatDay> repeatDays;
        private Boolean hasNotice;
        private EventCategory category;
        // 카테고리별 필드 (선택적)
        private String details;        // GENERAL, HOSPITAL, MEDICINE
        private String hospitalName;   // HOSPITAL
        private String department;     // HOSPITAL
        private Integer cost;          // HOSPITAL
        private String medicineName;   // MEDICINE
        private String distance;       // WALK
        private String duration;       // WALK
    }

    @Getter
    @Setter
    public static class EventUpdateRequest {
        private String title;
        private LocalDate date;
        private Boolean isRepeated;
        private LocalDate expiredDate;
        private Boolean hasNotice;
        private EventCategory category;
        private String details;
        private String hospitalName;
        private String department;
        private Integer cost;
        private String medicineName;
        private String distance;
        private String duration;

        public EventUpdateRequest(
                @JsonProperty("title") String title,
                @JsonProperty("date") LocalDate date,
                @JsonProperty("isRepeated") Boolean isRepeated,
                @JsonProperty("expiredDate") LocalDate expiredDate,
                @JsonProperty("hasNotice") Boolean hasNotice,
                @JsonProperty("category") EventCategory category,
                @JsonProperty("details") String details,
                @JsonProperty("hospitalName") String hospitalName,
                @JsonProperty("department") String department,
                @JsonProperty("cost") Integer cost,
                @JsonProperty("medicineName") String medicineName,
                @JsonProperty("distance") String distance,
                @JsonProperty("duration") String duration) {
            this.title = title;
            this.date = date;
            this.isRepeated = isRepeated;
            this.expiredDate = expiredDate;
            this.hasNotice = hasNotice;
            this.category = category;
            this.details = details;
            this.hospitalName = hospitalName;
            this.department = department;
            this.cost = cost;
            this.medicineName = medicineName;
            this.distance = distance;
            this.duration = duration;
        }
    }
}