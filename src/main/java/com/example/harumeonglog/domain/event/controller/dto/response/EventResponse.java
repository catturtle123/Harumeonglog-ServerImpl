package com.example.harumeonglog.domain.event.controller.dto.response;

import com.example.harumeonglog.domain.event.domain.*;
import com.example.harumeonglog.domain.event.domain.enums.EventCategory;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class EventResponse {
    @Getter
    @Builder
    public static class EventCreateResponse {
        private Long eventId;
        public static EventCreateResponse from(Event event) {
            return EventCreateResponse.builder()
                    .eventId(event.getId())
                    .build();
        }
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

        public static GeneralEventDetailResponse from(GeneralEvent event) {
            return GeneralEventDetailResponse.builder()
                    .id(event.getId())
                    .title(event.getTitle())
                    .date(event.getDate())
                    .isRepeated(event.getIsRepeated())
                    .expiredDate(event.getExpiredDate())
                    .hasNotice(event.getHasNotice())
                    .category(event.getCategory())
                    .details(event.getDetails())
                    .build();
        }
    }

    @Getter
    @SuperBuilder
    public static class HospitalEventDetailResponse extends BaseEventResponse {
        private String hospitalName;
        private String department;
        private Integer cost;
        private String details;

        public static HospitalEventDetailResponse from(HospitalEvent event) {
            return HospitalEventDetailResponse.builder()
                    .id(event.getId())
                    .title(event.getTitle())
                    .date(event.getDate())
                    .isRepeated(event.getIsRepeated())
                    .expiredDate(event.getExpiredDate())
                    .hasNotice(event.getHasNotice())
                    .category(event.getCategory())
                    .hospitalName(event.getHospitalName())
                    .department(event.getDepartment())
                    .cost(event.getCost())
                    .details(event.getDetails())
                    .build();
        }
    }

    @Getter
    @SuperBuilder
    public static class MedicineEventDetailResponse extends BaseEventResponse {
        private String medicineName;
        private String details;

        public static MedicineEventDetailResponse from(MedicineEvent event) {
            return MedicineEventDetailResponse.builder()
                    .id(event.getId())
                    .title(event.getTitle())
                    .date(event.getDate())
                    .isRepeated(event.getIsRepeated())
                    .expiredDate(event.getExpiredDate())
                    .hasNotice(event.getHasNotice())
                    .category(event.getCategory())
                    .medicineName(event.getMedicineName())
                    .details(event.getDetails())
                    .build();
        }
    }

    @Getter
    @SuperBuilder
    public static class WalkEventDetailResponse extends BaseEventResponse {
        private String distance;
        private String duration;

        public static WalkEventDetailResponse from(WalkEvent event) {
            return WalkEventDetailResponse.builder()
                    .id(event.getId())
                    .title(event.getTitle())
                    .date(event.getDate())
                    .isRepeated(event.getIsRepeated())
                    .expiredDate(event.getExpiredDate())
                    .hasNotice(event.getHasNotice())
                    .category(event.getCategory())
                    .distance(event.getDistance())
                    .duration(event.getDuration())
                    .build();
        }
    }

    @Getter
    @SuperBuilder
    public static class BathEventDetailResponse extends BaseEventResponse {
        public static BathEventDetailResponse from(BathEvent event) {
            return BathEventDetailResponse.builder()
                    .id(event.getId())
                    .title(event.getTitle())
                    .date(event.getDate())
                    .isRepeated(event.getIsRepeated())
                    .expiredDate(event.getExpiredDate())
                    .hasNotice(event.getHasNotice())
                    .category(event.getCategory())
                    .build();
        }
    }


    @Getter
    @Builder
    public static class EventDayResponse{
        private List<EventShortResponse> events;

        public static EventDayResponse from(List<Event> events){
            List<EventShortResponse> responses = events.stream().map(event -> {
                return EventShortResponse.builder()
                        .id(event.getId())
                        .title(event.getTitle())
                        .done(event.getDone())
                        .build();
            }).toList();
            return EventDayResponse.builder()
                    .events(responses)
                    .build();
        }
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

        public static EventCompleteResponse from(Event event) {
            return EventCompleteResponse.builder()
                    .eventId(event.getId())
                    .build();
        }
    }
}
