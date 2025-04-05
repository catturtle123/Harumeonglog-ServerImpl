package com.example.harumeonglog.restDocs.controller;

import com.example.harumeonglog.domain.member.domain.Member;
import com.example.harumeonglog.domain.member.domain.enums.SocialType;
import com.example.harumeonglog.domain.post.controller.PostController;
import com.example.harumeonglog.domain.post.controller.dto.request.PostRequest;
import com.example.harumeonglog.domain.post.controller.port.PostCommandService;
import com.example.harumeonglog.domain.post.controller.port.PostQueryService;
import com.example.harumeonglog.domain.post.domain.Post;
import com.example.harumeonglog.domain.post.domain.enums.PostCategory;
import com.example.harumeonglog.restDocs.base.AbstractRestDocsTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PostController.class)
public class PostControllerTest extends AbstractRestDocsTest {

    @MockitoBean
    private PostCommandService postCommandService;

    @MockitoBean
    private PostQueryService postQueryService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("게시글 리스트 조회")
    void getPosts() throws Exception {
        final Long cursor = 0L;
        final int size = 2;
        final Boolean hasNext = false;

        final Member member = Member.builder()
                .id(1L)
                .birth(LocalDate.of(2011,11,11))
                .email("a@naver.com")
                .createdAt(LocalDateTime.of(2022,11,11,0,0))
                .updatedAt(LocalDateTime.of(2022,11,11,0,0))
                .deletedAt(null)
                .socialType(SocialType.APPLE)
                .providerId("123")
                .nickname("jamey")
                .image("image")
                .build();

        final Post post1 = Post.builder()
                .id(1L)
                .content("내용")
                .category(PostCategory.INFO)
                .commentNum(0L)
                .postLikeNum(0L)
                .postReportNum(0L)
                .createdAt(LocalDateTime.of(2022,11,11,0,0))
                .deletedAt(null)
                .updatedAt(LocalDateTime.of(2022,11,11,0,0))
                .commentList(null)
                .postImageList(null)
                .member(member)
                .build();

        final Post post2 = Post.builder()
                .id(2L)
                .content("내용")
                .category(PostCategory.INFO)
                .commentNum(0L)
                .postLikeNum(0L)
                .postReportNum(0L)
                .createdAt(LocalDateTime.of(2023,11,11,0,0))
                .deletedAt(null)
                .updatedAt(LocalDateTime.of(2023,11,11,0,0))
                .commentList(null)
                .postImageList(null)
                .member(member)
                .build();

        Slice<Post> postSlice = new SliceImpl<>(List.of(post1, post2), PageRequest.of(0, size), hasNext);

        given(postQueryService.getPosts(anyLong(), anyInt())).willReturn(postSlice);

        ResultActions result = mockMvc.perform(get("/posts")
                .param("cursor", cursor.toString())
                .param("size", String.valueOf(size)));

        result.andExpect(status().isOk())
                .andDo(restDocs.document(
                        queryParameters(
                                parameterWithName("cursor").description("커서 (마지막으로 본 게시글 ID)").optional(),
                                parameterWithName("size").description("불러올 게시글 수").optional()
                        ),
                        commonResponse,
                        responseFields(
                                beneathPath("result").withSubsectionId("result"),
                                fieldWithPath("cursor").description("다음 요청에 사용할 커서"),
                                fieldWithPath("hasNext").description("다음 게시글이 더 있는지 여부"),
                                subsectionWithPath("items").description("게시글 리스트").type("PostPreviewResponse[]")
                        ),
                        responseFields(
                                beneathPath("result.items[]").withSubsectionId("PostPreviewResponse"),
                                fieldWithPath("postId").description("게시글 ID"),
                                fieldWithPath("content").description("게시글 내용").type("String"),
                                fieldWithPath("likeNum").description("좋아요 수"),
                                fieldWithPath("commentNum").description("댓글 수"),
                                fieldWithPath("postCategory").description("게시글 카테고리 (INFO, HUMOR, QNA, SNS, ETC)"),
                                subsectionWithPath("memberInfoResponse").description("작성자 정보").type("MemberInfo")
                        ),
                        responseFields(
                                beneathPath("result.items[].memberInfoResponse").withSubsectionId("MemberInfo"),
                                fieldWithPath("memberId").description("작성자 ID"),
                                fieldWithPath("email").description("작성자 이메일"),
                                fieldWithPath("nickname").description("작성자 닉네임"),
                                fieldWithPath("image").description("작성자 이미지 URL")
                        )
                ));
    }

    @Test
    @DisplayName("게시글 생성")
    void createPost() throws Exception {
        PostRequest.PostCreateRequest request = PostRequest.PostCreateRequest.builder()
                .postCategory(PostCategory.INFO)
                .content("내용")
                .postImageList(null)
                .build();

        String body = objectMapper.writeValueAsString(request);

        Member member = Member.builder()
                .id(1L)
                .email("a@naver.com")
                .nickname("jamey")
                .image("image")
                .build();

        given(postCommandService.createPost(any())).willReturn(
                Post.builder()
                        .id(1L)
                        .content("내용")
                        .postImageList(null)
                        .member(member)
                        .build()
        );

        ResultActions result = mockMvc.perform(post("/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body));

        result.andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestFields(
                                fieldWithPath("postCategory").description("게시글 카테고리"),
                                fieldWithPath("content").description("게시글 내용"),
                                fieldWithPath("postImageList").description("게시글 이미지 URL 목록").optional()
                        ),
                        commonResponse
                ));
    }

    @Test
    @DisplayName("게시글 수정")
    void updatePost() throws Exception {
        PostRequest.PostUpdateRequest request = PostRequest.PostUpdateRequest.builder()
                .postCategory(PostCategory.INFO)
                .postImageList(null)
                .content("바꿈")
                .build();

        String body = objectMapper.writeValueAsString(request);

        Member member = Member.builder()
                .id(1L)
                .email("a@naver.com")
                .nickname("jamey")
                .image("image")
                .build();

        given(postCommandService.updatePost(anyLong(), any())).willReturn(
                Post.builder()
                        .id(1L)
                        .postImageList(null)
                        .content("바꿈")
                        .member(member)
                        .build()
        );


        ResultActions result = mockMvc.perform(patch("/posts/{postId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body));

        result.andExpect(status().isOk())
                .andDo(restDocs.document(
                        pathParameters(
                                parameterWithName("postId").description("게시글 ID")
                        ),
                        requestFields(
                                fieldWithPath("postCategory").description("게시글 카테고리"),
                                fieldWithPath("content").description("게시글 내용"),
                                fieldWithPath("postImageList").description("게시글 이미지 URL 목록").optional()
                        ),
                        commonResponse,
                        responseFields(
                                beneathPath("result").withSubsectionId("PostPreviewResponse"),
                                fieldWithPath("postId").description("게시글 ID"),
                                fieldWithPath("content").description("게시글 내용"),
                                fieldWithPath("likeNum").description("게시글 좋아요 수"),
                                fieldWithPath("commentNum").description("게시글 댓글 수"),
                                fieldWithPath("postCategory").description("게시글 카테고리"),
                                subsectionWithPath("memberInfoResponse").description("작성자 정보").type("MemberInfoResponse")
                        ),
                        responseFields(
                                beneathPath("result.memberInfoResponse").withSubsectionId("MemberInfoResponse"),
                                fieldWithPath("memberId").description("작성자 ID"),
                                fieldWithPath("email").description("작성자 이메일"),
                                fieldWithPath("nickname").description("작성자 닉네임"),
                                fieldWithPath("image").description("작성자 이미지 URL")
                        )
                ));
    }

    @Test
    @DisplayName("게시글 삭제")
    void deletePost() throws Exception {
        ResultActions result = mockMvc.perform(delete("/posts/{postId}", 1L));

        result.andExpect(status().isOk())
                .andDo(restDocs.document(
                        pathParameters(
                                parameterWithName("postId").description("삭제할 게시글 ID")
                        ),
                        commonResponse
                ));
    }

    @Test
    @DisplayName("게시글 좋아요")
    void likePost() throws Exception {
        ResultActions result = mockMvc.perform(post("/posts/{postId}/likes", 1L));

        result.andExpect(status().isOk())
                .andDo(restDocs.document(
                        pathParameters(
                                parameterWithName("postId").description("좋아요 누를 게시글 ID")
                        ),
                        commonResponse
                ));
    }

    @Test
    @DisplayName("게시글 상세 조회")
    void getPost() throws Exception {
        Member member = Member.builder()
                .id(1L)
                .email("a@naver.com")
                .nickname("jamey")
                .image("image")
                .birth(LocalDate.of(2011, 11, 11))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .socialType(SocialType.APPLE)
                .providerId("123")
                .build();

        Post post = Post.builder()
                .id(1L)
                .content("상세 내용")
                .commentNum(3L)
                .postLikeNum(10L)
                .postReportNum(0L)
                .category(PostCategory.INFO)
                .postImageList(List.of())
                .member(member)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        given(postQueryService.getPost()).willReturn(post);

        ResultActions result = mockMvc.perform(get("/posts/{postId}", 1L));

        result.andExpect(status().isOk())
                .andDo(restDocs.document(
                        pathParameters(
                                parameterWithName("postId").description("상세 조회할 게시글 ID")
                        ),
                        commonResponse,
                        responseFields(
                                beneathPath("result").withSubsectionId("result"),
                                fieldWithPath("postId").description("게시글 ID"),
                                fieldWithPath("content").description("게시글 내용").optional(),
                                fieldWithPath("likeNum").description("좋아요 수"),
                                fieldWithPath("commentNum").description("댓글 수"),
                                fieldWithPath("postCategory").description("게시글 카테고리 (INFO, HUMOR, QNA, SNS, ETC)"),
                                fieldWithPath("postImages").description("게시글 이미지 URL 목록").optional(),
                                subsectionWithPath("memberInfoResponse").description("작성자 정보")
                        ),
                        responseFields(
                                beneathPath("result.memberInfoResponse").withSubsectionId("MemberInfo"),
                                fieldWithPath("memberId").description("작성자 ID"),
                                fieldWithPath("email").description("작성자 이메일"),
                                fieldWithPath("nickname").description("작성자 닉네임"),
                                fieldWithPath("image").description("작성자 이미지 URL")
                        )
                ));
    }

    @Test
    @DisplayName("게시물 신고")
    void reportPost() throws Exception {
        ResultActions result = mockMvc.perform(post("/posts/{postId}/reports", 1L));

        result.andExpect(status().isOk())
                .andDo(restDocs.document(
                        pathParameters(
                                parameterWithName("postId").description("post PK")
                        ),
                        commonResponse
                ));
    }

    @Test
    @DisplayName("내가 쓴 게시글 리스트 조회")
    void getMyPost() throws Exception {
        final Long cursor = 0L;
        final int size = 2;
        final Boolean hasNext = false;

        final Member member = Member.builder()
                .id(1L)
                .birth(LocalDate.of(2011,11,11))
                .email("a@naver.com")
                .createdAt(LocalDateTime.of(2022,11,11,0,0))
                .updatedAt(LocalDateTime.of(2022,11,11,0,0))
                .deletedAt(null)
                .socialType(SocialType.APPLE)
                .providerId("123")
                .nickname("jamey")
                .image("image")
                .build();

        final Post post1 = Post.builder()
                .id(1L)
                .content("내용")
                .category(PostCategory.INFO)
                .commentNum(0L)
                .postLikeNum(0L)
                .postReportNum(0L)
                .createdAt(LocalDateTime.of(2022,11,11,0,0))
                .deletedAt(null)
                .updatedAt(LocalDateTime.of(2022,11,11,0,0))
                .commentList(null)
                .postImageList(null)
                .member(member)
                .build();

        final Post post2 = Post.builder()
                .id(2L)
                .content("내용")
                .category(PostCategory.INFO)
                .commentNum(0L)
                .postLikeNum(0L)
                .postReportNum(0L)
                .createdAt(LocalDateTime.of(2023,11,11,0,0))
                .deletedAt(null)
                .updatedAt(LocalDateTime.of(2023,11,11,0,0))
                .commentList(null)
                .postImageList(null)
                .member(member)
                .build();

        Slice<Post> postSlice = new SliceImpl<>(List.of(post1, post2), PageRequest.of(0, size), hasNext);

        given(postQueryService.getMyPost(anyLong(), anyInt())).willReturn(postSlice);

        ResultActions result = mockMvc.perform(get("/posts/me")
                .param("cursor", cursor.toString())
                .param("size", String.valueOf(size)));

        result.andExpect(status().isOk())
                .andDo(restDocs.document(
                        queryParameters(
                                parameterWithName("cursor").description("커서 (마지막으로 본 게시글 ID)").optional(),
                                parameterWithName("size").description("불러올 게시글 수").optional()
                        ),
                        commonResponse,
                        responseFields(
                                beneathPath("result").withSubsectionId("result"),
                                fieldWithPath("cursor").description("다음 요청에 사용할 커서"),
                                fieldWithPath("hasNext").description("다음 게시글이 더 있는지 여부"),
                                subsectionWithPath("items").description("게시글 리스트").type("PostPreviewResponse[]")
                        ),
                        responseFields(
                                beneathPath("result.items[]").withSubsectionId("PostPreviewResponse"),
                                fieldWithPath("postId").description("게시글 ID"),
                                fieldWithPath("content").description("게시글 내용").type("String"),
                                fieldWithPath("likeNum").description("좋아요 수"),
                                fieldWithPath("commentNum").description("댓글 수"),
                                fieldWithPath("postCategory").description("게시글 카테고리 (INFO, HUMOR, QNA, SNS, ETC)"),
                                subsectionWithPath("memberInfoResponse").description("작성자 정보").type("MemberInfo")
                        ),
                        responseFields(
                                beneathPath("result.items[].memberInfoResponse").withSubsectionId("MemberInfo"),
                                fieldWithPath("memberId").description("작성자 ID"),
                                fieldWithPath("email").description("작성자 이메일"),
                                fieldWithPath("nickname").description("작성자 닉네임"),
                                fieldWithPath("image").description("작성자 이미지 URL")
                        )
                ));
    }

    @Test
    @DisplayName("내가 좋아요 단 게시글 리스트 조회")
    void getMyLikePost() throws Exception {
        final Long cursor = 0L;
        final int size = 2;
        final Boolean hasNext = false;

        final Member member = Member.builder()
                .id(1L)
                .birth(LocalDate.of(2011,11,11))
                .email("a@naver.com")
                .createdAt(LocalDateTime.of(2022,11,11,0,0))
                .updatedAt(LocalDateTime.of(2022,11,11,0,0))
                .deletedAt(null)
                .socialType(SocialType.APPLE)
                .providerId("123")
                .nickname("jamey")
                .image("image")
                .build();

        final Post post1 = Post.builder()
                .id(1L)
                .content("내용")
                .category(PostCategory.INFO)
                .commentNum(0L)
                .postLikeNum(0L)
                .postReportNum(0L)
                .createdAt(LocalDateTime.of(2022,11,11,0,0))
                .deletedAt(null)
                .updatedAt(LocalDateTime.of(2022,11,11,0,0))
                .commentList(null)
                .postImageList(null)
                .member(member)
                .build();

        final Post post2 = Post.builder()
                .id(2L)
                .content("내용")
                .category(PostCategory.INFO)
                .commentNum(0L)
                .postLikeNum(0L)
                .postReportNum(0L)
                .createdAt(LocalDateTime.of(2023,11,11,0,0))
                .deletedAt(null)
                .updatedAt(LocalDateTime.of(2023,11,11,0,0))
                .commentList(null)
                .postImageList(null)
                .member(member)
                .build();

        Slice<Post> postSlice = new SliceImpl<>(List.of(post1, post2), PageRequest.of(0, size), hasNext);

        given(postQueryService.getMyLikePost(anyLong(), anyInt())).willReturn(postSlice);

        ResultActions result = mockMvc.perform(get("/posts/me/likes")
                .param("cursor", cursor.toString())
                .param("size", String.valueOf(size)));

        result.andExpect(status().isOk())
                .andDo(restDocs.document(
                        queryParameters(
                                parameterWithName("cursor").description("커서 (마지막으로 본 게시글 ID)").optional(),
                                parameterWithName("size").description("불러올 게시글 수").optional()
                        ),
                        commonResponse,
                        responseFields(
                                beneathPath("result").withSubsectionId("result"),
                                fieldWithPath("cursor").description("다음 요청에 사용할 커서"),
                                fieldWithPath("hasNext").description("다음 게시글이 더 있는지 여부"),
                                subsectionWithPath("items").description("게시글 리스트").type("PostPreviewResponse[]")
                        ),
                        responseFields(
                                beneathPath("result.items[]").withSubsectionId("PostPreviewResponse"),
                                fieldWithPath("postId").description("게시글 ID"),
                                fieldWithPath("content").description("게시글 내용").type("String"),
                                fieldWithPath("likeNum").description("좋아요 수"),
                                fieldWithPath("commentNum").description("댓글 수"),
                                fieldWithPath("postCategory").description("게시글 카테고리 (INFO, HUMOR, QNA, SNS, ETC)"),
                                subsectionWithPath("memberInfoResponse").description("작성자 정보").type("MemberInfo")
                        ),
                        responseFields(
                                beneathPath("result.items[].memberInfoResponse").withSubsectionId("MemberInfo"),
                                fieldWithPath("memberId").description("작성자 ID"),
                                fieldWithPath("email").description("작성자 이메일"),
                                fieldWithPath("nickname").description("작성자 닉네임"),
                                fieldWithPath("image").description("작성자 이미지 URL")
                        )
                ));
    }
}
