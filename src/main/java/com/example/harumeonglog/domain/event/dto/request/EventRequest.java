package com.example.harumeonglog.domain.event.dto.request;

import com.example.harumeonglog.domain.event.entity.enums.EventCategory;
import com.example.harumeonglog.domain.event.entity.enums.RepeatDay;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class EventRequest {

    @Getter
    @Builder
    public static class EventRequestDTO {

        @Schema(description = "이벤트 제목", example = "강아지 산책", required = true)
        private String title;

        @JsonFormat(pattern = "yyyy-MM-dd")
        @Schema(description = "이벤트 날짜", example = "2024-07-09", type = "string", pattern = "yyyy-MM-dd")
        private LocalDate date;

        @Schema(description = "반복 여부", example = "true")
        private Boolean isRepeated;

        @JsonFormat(pattern = "yyyy-MM-dd")
        @Schema(description = "반복 종료 날짜", example = "2024-12-31", type = "string", pattern = "yyyy-MM-dd")
        private LocalDate expiredDate;

        @Schema(description = "반복 요일 목록", example = "[\"MON\", \"WED\", \"FRI\"]")
        private List<RepeatDay> repeatDays;

        @Schema(description = "알림 여부", example = "true")
        private Boolean hasNotice;

        @JsonFormat(pattern = "HH:mm:ss")
        @Schema(description = "이벤트 시간", example = "14:30:00", type = "string", pattern = "HH:mm:ss")
        private LocalTime time;

        @Schema(description = "이벤트 카테고리", example = "WALK",
                allowableValues = {"GENERAL", "WALK", "HOSPITAL", "MEDICINE", "BATH"})
        private EventCategory category;

        // 카테고리별 필드 (선택적)
        @Schema(description = "상세 내용 (GENERAL, HOSPITAL, MEDICINE, WALK)", example = "공원에서 30분간 산책")
        private String details;

        @Schema(description = "병원명 (HOSPITAL 전용)", example = "24시 동물병원")
        private String hospitalName;

        @Schema(description = "진료과 (HOSPITAL 전용)", example = "내과")
        private String department;

        @Schema(description = "진료비 (HOSPITAL 전용)", example = "50000")
        private Integer cost;

        @Schema(description = "약물명 (MEDICINE 전용)", example = "항생제")
        private String medicineName;

        @Schema(description = "산책 거리 (WALK 전용)", example = "2km")
        private String distance;

        @Schema(description = "산책 시간 (WALK 전용)", example = "30분")
        private String duration;
    }
}