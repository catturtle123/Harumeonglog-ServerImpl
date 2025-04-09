package com.example.harumeonglog.restDocs.controller;

import com.example.harumeonglog.domain.event.controller.EventController;
import com.example.harumeonglog.domain.event.controller.dto.request.EventRequest;
import com.example.harumeonglog.domain.event.controller.dto.response.EventResponse;
import com.example.harumeonglog.domain.event.controller.port.EventService;
import com.example.harumeonglog.domain.event.domain.Event;
import com.example.harumeonglog.domain.event.domain.WalkEvent;
import com.example.harumeonglog.domain.event.domain.enums.EventCategory;
import com.example.harumeonglog.restDocs.base.AbstractRestDocsTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@WebMvcTest(EventController.class)
public class EventControllerTest extends AbstractRestDocsTest {

    @MockitoBean
    private EventService eventService;

    @Autowired
    private ObjectMapper objectMapper;
    @Test
    @DisplayName("이벤트 생성")
    void createEvent() throws Exception {
        // given
        EventRequest.EventCreateRequest request = new EventRequest.EventCreateRequest(
                "강아지 산책",
                LocalDate.of(2025, 3, 30),
                false,
                null,
                true,
                EventCategory.WALK,
                null,           // details
                null,           // hospitalName
                null,           // department
                null,           // cost
                null,           // medicineName
                "2km",          // distance
                "30분"          // duration
        );

        WalkEvent event =
                WalkEvent.builder()
                        .id(1L)
                        .title("강아지 산책")
                        .category(EventCategory.WALK)
                        .distance("2km")
                        .duration("30분")
                        .build();

        given(eventService.createEvent(any(EventRequest.EventCreateRequest.class)))
                .willReturn(EventResponse.EventCreateResponse.from(event));

        String requestBody = objectMapper.writeValueAsString(request);

        // when
        ResultActions result = mockMvc.perform(post("/events")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody));



        // then
        result
                .andExpect(status().isCreated())  // 201 기대
                .andDo(restDocs.document(
                        requestFields(
                                fieldWithPath("title").description("일정 제목"),
                                fieldWithPath("date").description("일정 날짜").type("LocalDate"),
                                fieldWithPath("isRepeated").description("반복 여부"),
                                fieldWithPath("expiredDate").description("만료 날짜 (반복 일정인 경우)").type("LocalDate").optional(),
                                fieldWithPath("hasNotice").description("알림 설정 여부"),
                                fieldWithPath("category").description("일정 카테고리"),
                                fieldWithPath("details").description("기타, 진료, 약 카테고리의 세부 내용").optional(),
                                fieldWithPath("hospitalName").description("진료 카테고리의 병원명").optional(),
                                fieldWithPath("department").description("진료 카테고리의 진료과목").optional(),
                                fieldWithPath("cost").description("진료 카테고리의 진료비").type("Integer").optional(),
                                fieldWithPath("medicineName").description("약 카테고리의 약 이름").optional(),
                                fieldWithPath("distance").description("산책 카테고리의 거리").optional(),
                                fieldWithPath("duration").description("산책 카테고리의 소요시간").optional()
                        ),
                        commonResponse,
                        responseFields(
                                beneathPath("result").withSubsectionId("result"),
                                fieldWithPath("eventId").description("생성된 이벤트 ID")
                        )

                ));
    }

    @Test
    @DisplayName("특정 날짜 기타 이벤트 목록 조회")
    void getDayEvent() throws Exception {
        // given
        String date = "2025-03-30";
        EventResponse.EventDayResponse response = EventResponse.EventDayResponse.builder()
                .events(List.of(
                        EventResponse.EventShortResponse.builder()
                                .id(1L)
                                .title("기타 일정")
                                .done(false)
                                .build(),
                        EventResponse.EventShortResponse.builder()
                                .id(2L)
                                .title("강아지 진료")
                                .done(true)
                                .build()
                ))
                .build();

        given(eventService.getDayEvents(anyString())).willReturn(response);

        // when
        ResultActions result = mockMvc.perform(get("/events")
                .param("date", date));

        // then
        result
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        queryParameters(
                                parameterWithName("date").description("조회할 날짜 (YYYY-MM-DD 형식)")
                        ),
                        commonResponse,
                        responseFields(
                                beneathPath("result").withSubsectionId("result"),
                                subsectionWithPath("events").description("일정 목록").type("EventShortResponse[]")
                        ),
                        responseFields(
                                beneathPath("result.events[]").withSubsectionId("EventShortResponse"),
                                fieldWithPath("id").description("일정 ID"),
                                fieldWithPath("title").description("일정 제목"),
                                fieldWithPath("done").description("완료 여부")
                        )
                ));
    }

    @Test
    @DisplayName("이벤트 상세 조회")
    void getEvent() throws Exception {
        // given
        Long eventId = 1L;
        given(eventService.getEvent(anyLong()))
                .willReturn(EventResponse.WalkEventDetailResponse.builder()
                        .id(1L)
                        .title("강아지 산책")
                        .date(LocalDate.of(2025, 3, 30))
                        .isRepeated(false)
                        .hasNotice(true)
                        .category(EventCategory.WALK)
                        .distance("2km")
                        .duration("30분")
                        .build());

        // when
        ResultActions result = mockMvc.perform(get("/events/{eventId}", eventId));

        // then
        result
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        pathParameters(
                                parameterWithName("eventId").description("조회할 이벤트 ID")
                        ),
                        commonResponse,
                        responseFields(
                                beneathPath("result").withSubsectionId("result"),
                                fieldWithPath("id").description("이벤트 ID"),
                                fieldWithPath("title").description("이벤트 제목"),
                                fieldWithPath("date").description("이벤트 날짜").type("LocalDate"),
                                fieldWithPath("isRepeated").description("반복 여부"),
                                fieldWithPath("expiredDate").description("만료 날짜").type("LocalDate").optional(),
                                fieldWithPath("hasNotice").description("알림 설정 여부"),
                                fieldWithPath("category").description("이벤트 카테고리"),
                                fieldWithPath("distance").description("산책 카테고리의 거리"),
                                fieldWithPath("duration").description("산책 카테고리의 소요시간")
                        )
                ));
    }

    @Test
    @DisplayName("이벤트 수정")
    void updateEvent() throws Exception {
        // given
        Long eventId = 1L;
        EventRequest.EventUpdateRequest request = new EventRequest.EventUpdateRequest(
                "강아지 산책 수정",
                LocalDate.of(2025, 3, 31),
                false,
                null,
                true,
                EventCategory.WALK,
                null,           // details
                null,           // hospitalName
                null,           // department
                null,           // cost
                null,           // medicineName
                "3km",          // distance
                "40분"          // duration
        );

        given(eventService.updateEvent(anyLong(), any(EventRequest.EventUpdateRequest.class)))
                .willReturn(EventResponse.WalkEventDetailResponse.builder()
                        .id(1L)
                        .title("강아지 산책 수정")
                        .date(LocalDate.of(2025, 3, 31))
                        .isRepeated(false)
                        .hasNotice(true)
                        .category(EventCategory.WALK)
                        .distance("3km")
                        .duration("40분")
                        .build());

        String requestBody = objectMapper.writeValueAsString(request);

        // when
        ResultActions result = mockMvc.perform(put("/events/{eventId}", eventId)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody));

        // then
        result
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        pathParameters(
                                parameterWithName("eventId").description("수정할 이벤트 ID")
                        ),
                        requestFields(
                                fieldWithPath("title").description("이벤트 제목"),
                                fieldWithPath("date").description("이벤트 날짜").type("LocalDate"),
                                fieldWithPath("isRepeated").description("반복 여부"),
                                fieldWithPath("expiredDate").description("만료 날짜 (반복 이벤트인 경우)").type("LocalDate").optional(),
                                fieldWithPath("hasNotice").description("알림 설정 여부"),
                                fieldWithPath("category").description("이벤트 카테고리"),
                                fieldWithPath("details").description("기타, 진료, 약 카테고리의 세부 내용").optional(),
                                fieldWithPath("hospitalName").description("진료 카테고리의 병원명").optional(),
                                fieldWithPath("department").description("진료 카테고리의 진료과목").optional(),
                                fieldWithPath("cost").description("진료 카테고리의 진료비").type("Integer").optional(),
                                fieldWithPath("medicineName").description("약 카테고리의 약 이름").optional(),
                                fieldWithPath("distance").description("산책 카테고리의 거리").optional(),
                                fieldWithPath("duration").description("산책 카테고리의 소요시간").optional()
                        ),
                        commonResponse,
                        responseFields(
                                beneathPath("result").withSubsectionId("result"),
                                fieldWithPath("id").description("이벤트 ID"),
                                fieldWithPath("title").description("이벤트 제목"),
                                fieldWithPath("date").description("이벤트 날짜").type("LocalDate"),
                                fieldWithPath("isRepeated").description("반복 여부"),
                                fieldWithPath("expiredDate").description("만료 날짜").type("LocalDate").optional(),
                                fieldWithPath("hasNotice").description("알림 설정 여부"),
                                fieldWithPath("category").description("이벤트 카테고리"),
                                fieldWithPath("distance").description("산책 카테고리의 거리"),
                                fieldWithPath("duration").description("산책 카테고리의 소요시간")
                        )
                ));
    }

    @Test
    @DisplayName("이벤트 삭제")
    void deleteEvent() throws Exception {
        // given
        Long eventId = 1L;
        doNothing().when(eventService).deleteEvent(anyLong());

        // when
        ResultActions result = mockMvc.perform(delete("/events/{eventId}", eventId));

        // then
        result
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        pathParameters(
                                parameterWithName("eventId").description("삭제할 이벤트 ID")
                        ),
                        responseFields(
                                fieldWithPath("isSuccess").description("요청 성공 여부").type(JsonFieldType.BOOLEAN),
                                fieldWithPath("code").description("응답 코드").type(JsonFieldType.STRING),
                                fieldWithPath("message").description("응답 메시지").type(JsonFieldType.STRING),
                                fieldWithPath("result").description("삭제 완료 메시지").type(JsonFieldType.STRING)
                        )
                ));
    }

    @Test
    @DisplayName("이벤트 완료 처리")
    void completeEvent() throws Exception {
        // given
        Long eventId = 1L;
        Event event = Event.builder()
                .id(eventId)
                .build();

        given(eventService.completeEvent(anyLong()))
                .willReturn(EventResponse.EventCompleteResponse.from(event));

        // when
        ResultActions result = mockMvc.perform(patch("/events/{eventId}", eventId));

        // then
        result
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        pathParameters(
                                parameterWithName("eventId").description("완료 처리할 이벤트 ID")
                        ),
                        commonResponse,
                        responseFields(
                                beneathPath("result").withSubsectionId("result"),
                                fieldWithPath("eventId").description("완료 처리된 이벤트 ID")
                        )
                ));
    }
}