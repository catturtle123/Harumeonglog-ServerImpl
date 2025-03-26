package com.example.harumeonglog.restDocs.controller;

import com.example.harumeonglog.domain.event.controller.EventController;
import com.example.harumeonglog.domain.event.controller.dto.request.EventRequest;
import com.example.harumeonglog.domain.event.controller.dto.response.EventResponse;
import com.example.harumeonglog.domain.event.controller.port.EventService;
import com.example.harumeonglog.domain.event.domain.Event;
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
                "공원에서 산책",
                LocalDate.of(2025, 3, 30),
                false,
                null,
                true,
                EventCategory.WALK
        );

        Event event = new Event();
        event.setId(1L);
        event.setTitle("강아지 산책");

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
                                fieldWithPath("title").description("이벤트 제목"),
                                fieldWithPath("details").description("이벤트 상세 내용"),
                                fieldWithPath("date").description("이벤트 날짜").type("LocalDate"),
                                fieldWithPath("isRepeated").description("반복 여부"),
                                fieldWithPath("expiredDate").description("만료 날짜 (반복 이벤트인 경우)").type("LocalDate").optional(),
                                fieldWithPath("hasNotice").description("알림 설정 여부"),
                                fieldWithPath("category").description("이벤트 카테고리")
                        ),
                        commonResponse,
                        responseFields(
                                beneathPath("result").withSubsectionId("result"),
                                fieldWithPath("eventId").description("생성된 이벤트 ID")
                        )
                ));
    }

    @Test
    @DisplayName("특정 날짜 이벤트 목록 조회")
    void getDayEvent() throws Exception {
        // given
        String date = "2025-03-30";
        given(eventService.getDayEvents(anyString()))
                .willReturn(EventResponse.EventListResponse.builder()
                        .events(List.of(
                                EventResponse.EventDetailResponse.builder()
                                        .id(1L)
                                        .title("강아지 산책")
                                        .details("공원에서 산책")
                                        .date(LocalDate.of(2025, 3, 30))
                                        .isRepeated(false)
                                        .hasNotice(true)
                                        .category(EventCategory.WALK)
                                        .build()
                        ))
                        .build());

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
                                subsectionWithPath("events").description("이벤트 목록").type("EventDetailResponse[]")
                        ),
                        responseFields(
                                beneathPath("result.events").withSubsectionId("EventDetailResponse"),
                                fieldWithPath("id").description("이벤트 ID"),
                                fieldWithPath("title").description("이벤트 제목"),
                                fieldWithPath("details").description("이벤트 상세 내용"),
                                fieldWithPath("date").description("이벤트 날짜").type("LocalDate"),
                                fieldWithPath("isRepeated").description("반복 여부"),
                                fieldWithPath("expiredDate").description("만료 날짜").type("LocalDate").optional(),
                                fieldWithPath("hasNotice").description("알림 설정 여부"),
                                fieldWithPath("category").description("이벤트 카테고리")
                        )
                ));
    }

    @Test
    @DisplayName("이벤트 상세 조회")
    void getEvent() throws Exception {
        // given
        Long eventId = 1L;
        given(eventService.getEvent(anyLong()))
                .willReturn(EventResponse.EventDetailResponse.builder()
                        .id(1L)
                        .title("강아지 산책")
                        .details("공원에서 산책")
                        .date(LocalDate.of(2025, 3, 30))
                        .isRepeated(false)
                        .hasNotice(true)
                        .category(EventCategory.WALK)
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
                                fieldWithPath("details").description("이벤트 상세 내용"),
                                fieldWithPath("date").description("이벤트 날짜").type("LocalDate"),
                                fieldWithPath("isRepeated").description("반복 여부"),
                                fieldWithPath("expiredDate").description("만료 날짜").type("LocalDate").optional(),
                                fieldWithPath("hasNotice").description("알림 설정 여부"),
                                fieldWithPath("category").description("이벤트 카테고리")
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
                "공원에서 산책 수정",
                LocalDate.of(2025, 3, 31),
                false,
                null,
                true,
                EventCategory.WALK
        );

        given(eventService.updateEvent(anyLong(), any(EventRequest.EventUpdateRequest.class)))
                .willReturn(EventResponse.EventUpdateResponse.builder()
                        .eventId(1L)
                        .title("강아지 산책 수정")
                        .details("공원에서 산책 수정")
                        .date(LocalDate.of(2025, 3, 31))
                        .isRepeated(false)
                        .hasNotice(true)
                        .category(EventCategory.WALK)
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
                                fieldWithPath("details").description("이벤트 상세 내용"),
                                fieldWithPath("date").description("이벤트 날짜").type("LocalDate"),
                                fieldWithPath("isRepeated").description("반복 여부"),
                                fieldWithPath("expiredDate").description("만료 날짜 (반복 이벤트인 경우)").type("LocalDate").optional(),
                                fieldWithPath("hasNotice").description("알림 설정 여부"),
                                fieldWithPath("category").description("이벤트 카테고리")
                        ),
                        commonResponse,
                        responseFields(
                                beneathPath("result").withSubsectionId("result"),
                                fieldWithPath("eventId").description("이벤트 ID"),
                                fieldWithPath("title").description("이벤트 제목"),
                                fieldWithPath("details").description("이벤트 상세 내용"),
                                fieldWithPath("date").description("이벤트 날짜").type("LocalDate"),
                                fieldWithPath("isRepeated").description("반복 여부"),
                                fieldWithPath("expiredDate").description("만료 날짜").type("LocalDate").optional(),
                                fieldWithPath("hasNotice").description("알림 설정 여부"),
                                fieldWithPath("category").description("이벤트 카테고리")
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
        Event event = new Event();
        event.setId(1L);

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