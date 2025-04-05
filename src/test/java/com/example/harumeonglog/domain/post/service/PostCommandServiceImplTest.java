package com.example.harumeonglog.domain.post.service;

import com.example.harumeonglog.domain.post.controller.dto.request.PostRequest;
import com.example.harumeonglog.domain.post.controller.dto.request.PostRequest.PostCreateRequest;
import com.example.harumeonglog.domain.post.controller.port.PostCommandService;
import com.example.harumeonglog.domain.post.domain.*;
import com.example.harumeonglog.domain.post.domain.enums.PostCategory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

class PostCommandServiceImplTest {

    private PostCommandServiceImpl postCommandService;

    @BeforeEach
    void init() {
        postCommandService = PostCommandServiceImpl.builder().build();
    }

    @Test
    @DisplayName("제대로 게시물이 생성되는 가")
    void canCreatePost() {
        //given
        List<String> postImageList = List.of("image1", "image2");

        PostCreateRequest postCreateRequest = PostCreateRequest.builder()
                .postCategory(PostCategory.INFO)
                .postImageList(postImageList)
                .content("test content")
                .build();

        PostImage postImage = new PostImage();

        //when
        Post post = postCommandService.createPost(postCreateRequest);

        //then
        assertThat(post).isNull();
    }

    @Test
    @DisplayName("제대로 게시물이 수정되는 가")
    void canUpdatePost() {
        //given
        List<String> postImageList = List.of("changeImage1", "changeImage2");

        PostRequest.PostUpdateRequest postUpdateRequest = PostRequest.PostUpdateRequest.builder()
                .postCategory(PostCategory.ETC)
                .postImageList(postImageList)
                .content("test change content")
                .build();

        //when
        Post post = postCommandService.updatePost(1L, postUpdateRequest);

        //then
        assertThat(post).isNull();
    }

    @Test
    @DisplayName("제대로 게시물이 삭제되는 가")
    void canDeletePost() {
        //given

        //when
        postCommandService.deletePost(1L);

        //then
    }

    @Test
    @DisplayName("제대로 게시물이 좋아요 되는 가")
    void canLikePost() {
        //given
        PostLike postLike = new PostLike();

        //when
        postCommandService.likePost(1L);

        //then
    }

    @Test
    @DisplayName("제대로 게시물이 신고 되는 가")
    void canReportPost() {
        //given
        PostReport postReport = new PostReport();
        PostBlock postBlock = new PostBlock();

        //when
        postCommandService.reportPost(1L);

        //then
    }

}