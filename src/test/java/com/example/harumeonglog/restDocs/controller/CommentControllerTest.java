package com.example.harumeonglog.restDocs.controller;

import com.example.harumeonglog.domain.comment.controller.CommentController;
import com.example.harumeonglog.domain.comment.controller.port.CommentService;
import com.example.harumeonglog.domain.comment.domain.Comment;
import com.example.harumeonglog.domain.member.domain.Member;
import com.example.harumeonglog.restDocs.base.AbstractRestDocsTest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.data.domain.SliceImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentController.class)
public class CommentControllerTest extends AbstractRestDocsTest {

    @MockitoBean
    private CommentService commentService;

    @Test
    void getComments() throws Exception {
        Comment comment = Comment.builder()
                .id(1L)
                .content("Test comment")
                .member(Member.builder().id(1L).nickname("testuser").build())
                .build();

        Mockito.when(commentService.getComments(eq(1L), any(), any()))
                .thenReturn(new SliceImpl<>(List.of(comment)));

        mockMvc.perform(get("/posts/{postId}/comments", 1L)
                        .param("cursor", "0")
                        .param("size", "10")
                )
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        pathParameters(
                                parameterWithName("postId").description("게시글 ID")
                        ),
                        queryParameters(
                                parameterWithName("cursor").optional().description("커서 기반 페이징 (댓글 ID)"),
                                parameterWithName("size").optional().description("페이지 크기")
                        ),
                        commonResponse,
                        responseFields(
                                beneathPath("result").withSubsectionId("result"),
                                subsectionWithPath("items").description("알림 목록들").type("CommentListResponse[]"),
                                fieldWithPath("hasNext").description("다음 페이지 존재 여부"),
                                fieldWithPath("cursor").description("다음 페이지 커서")
                        ),
                        responseFields(
                                beneathPath("result.items[]").withSubsectionId("CommentListResponse"),
                                fieldWithPath("commentId").description("작성자 PK"),
                                fieldWithPath("content").description("댓글 내용"),
                                subsectionWithPath("memberInfoResponse").description("사용자 정보").type("MemberInfoResponse")
                        ),
                        responseFields(
                                beneathPath("result.items[].memberInfoResponse").withSubsectionId("MemberInfoResponse"),
                                fieldWithPath("memberId").description("member PK"),
                                fieldWithPath("email").description("member email"),
                                fieldWithPath("nickname").description("member nickname"),
                                fieldWithPath("image").description("member 프로필 이미지")
                        )
                ));
    }

    @Test
    void createComment() throws Exception {
        Mockito.when(commentService.createComment(any()))
                .thenReturn(Comment.builder().id(1L).build());

        String requestBody = """
        {
          "content": "테스트 댓글입니다"
        }
    """;

        mockMvc.perform(post("/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestFields(
                                fieldWithPath("content").description("댓글 내용")
                        ),
                        responseFields(
                                fieldWithPath("isSuccess").description("요청 성공 여부"),
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("result").description("생성된 댓글 ID")
                        )
                ));
    }


    @Test
    void deleteComment() throws Exception {
        mockMvc.perform(delete("/comments/{commentId}", 1L))
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        pathParameters(
                                parameterWithName("commentId").description("댓글 ID")
                        ),
                        commonResponse
                ));
    }

    @Test
    void reportComment() throws Exception {
        mockMvc.perform(post("/comments/{commentId}/reports", 1L))
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        pathParameters(
                                parameterWithName("commentId").description("신고할 댓글 ID")
                        ),
                        commonResponse
                ));
    }

    @Test
    void blockComment() throws Exception {
        mockMvc.perform(post("/comments/{commentId}/blocks", 1L))
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        pathParameters(
                                parameterWithName("commentId").description("차단할 댓글 ID")
                        ),
                        commonResponse
                ));
    }
}
