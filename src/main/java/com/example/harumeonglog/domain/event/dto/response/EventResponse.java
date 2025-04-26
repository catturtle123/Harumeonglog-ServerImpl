package com.example.harumeonglog.domain.event.dto.response;


import com.example.harumeonglog.domain.event.entity.enums.EventCategory;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class EventResponse {
    @Getter
    @Builder
    public static class EventCreateResponse {
        private Long eventId;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }
    @Getter
    @SuperBuilder
    public static abstract class BaseEventResponse {
        private Long id;
        private String title;
        private LocalDate date;
        private Boolean isRepeated;
        private LocalDate expiredDate;
        private Boolean hasNotice;
        private EventCategory category;
    }

    @Getter
    @SuperBuilder
    public static class GeneralEventDetailResponse extends BaseEventResponse {
        private String details;


    }

    @Getter
    @SuperBuilder
    public static class HospitalEventDetailResponse extends BaseEventResponse {
        private String hospitalName;
        private String department;
        private Integer cost;
        private String details;


    }

    @Getter
    @SuperBuilder
    public static class MedicineEventDetailResponse extends BaseEventResponse {
        private String medicineName;
        private String details;


    }

    @Getter
    @SuperBuilder
    public static class WalkEventDetailResponse extends BaseEventResponse {
        private String distance;
        private String duration;


    }

    @Getter
    @SuperBuilder
    public static class BathEventDetailResponse extends BaseEventResponse {

    }


    @Getter
    @Builder
    public static class EventDayResponse{
        private List<EventShortResponse> events;


    }

    @Getter
    @Builder
    public static class EventShortResponse{
        private Long id;
        private String title;
        private Boolean done;
    }

    @Getter
    @Builder
    public static class EventCompleteResponse {
        private Long eventId;

    }
}
