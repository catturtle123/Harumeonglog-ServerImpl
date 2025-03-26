package com.example.harumeonglog.restDocs.controller;

import com.example.harumeonglog.domain.member.controller.NoticeController;
import com.example.harumeonglog.domain.member.controller.port.NoticeService;
import com.example.harumeonglog.domain.member.domain.Notice;
import com.example.harumeonglog.domain.member.domain.enums.NoticeType;
import com.example.harumeonglog.restDocs.base.AbstractRestDocsTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.stream.IntStream;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(NoticeController.class)
public class NoticeControllerTest extends AbstractRestDocsTest {

    @MockitoBean
    private NoticeService noticeService;

    @Test
    @DisplayName("알림 목록 조회")
    void getNotices() throws Exception {
        // given
        int size = 10;
        long cursor = 0L;
        boolean hasNext = false;

        List<Notice> notices = IntStream.range(1, 11)
                .mapToObj(i -> Notice.builder()
                        .id((long) i)
                        .title("알림 제목 " + i)
                        .content("알림 내용 " + i)
                        .noticeType(NoticeType.COMMENT)
                        .targetId(100L + i)
                        .build())
                .toList();

        Slice<Notice> noticeSlice = new SliceImpl<>(notices, PageRequest.of(0, size), hasNext);
        given(noticeService.getNotices(size, cursor)).willReturn(noticeSlice);

        // when
        ResultActions result = mockMvc.perform(get("/notices")
                .param("size", String.valueOf(size))
                .param("cursor", String.valueOf(cursor))
                .accept(MediaType.APPLICATION_JSON));

        // then
        result.andExpect(status().isOk())
                .andDo(restDocs.document(
                        queryParameters(
                                parameterWithName("size").description("불러올 알림 개수"),
                                parameterWithName("cursor").description("현재 커서 (마지막 알림 ID)")
                        ),
                        commonResponse,
                        responseFields(
                                beneathPath("result").withSubsectionId("result"),
                                subsectionWithPath("items").description("알림 목록들").type("NoticeListResponse[]"),
                                fieldWithPath("hasNext").description("다음 페이지 존재 여부"),
                                fieldWithPath("cursor").description("다음 페이지 커서")
                        ),
                        responseFields(
                                beneathPath("result.items").withSubsectionId("NoticeListResponse"),
                                fieldWithPath("noticeId").description("알림 ID"),
                                fieldWithPath("title").description("알림 제목"),
                                fieldWithPath("content").description("알림 내용"),
                                fieldWithPath("noticeType").description("알림 타입"),
                                fieldWithPath("targetId").description("관련 리소스 ID")
                        )
                ));
    }

    @Test
    @DisplayName("알림 삭제")
    void deleteNotice() throws Exception {
        // given
        Long alarmId = 1L;
        willDoNothing().given(noticeService).deleteNotice(alarmId);

        // when
        ResultActions result = mockMvc.perform(delete("/notices/{noticeId}", alarmId));

        // then
        result.andExpect(status().isOk())
                .andDo(restDocs.document(
                        pathParameters(
                                parameterWithName("noticeId").description("삭제할 알림 ID")
                        ),
                        commonResponse
                ));
    }

}