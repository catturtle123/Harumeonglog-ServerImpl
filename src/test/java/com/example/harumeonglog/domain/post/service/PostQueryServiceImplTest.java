package com.example.harumeonglog.domain.post.service;

import com.example.harumeonglog.domain.post.controller.port.PostQueryService;
import com.example.harumeonglog.domain.post.domain.Post;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Slice;
import static org.assertj.core.api.Assertions.assertThat;

import static org.junit.jupiter.api.Assertions.*;

class PostQueryServiceImplTest {

    private PostQueryService postQueryService;

    @BeforeEach
    void init() {
        this.postQueryService = PostQueryServiceImpl.builder().build();
    }

    @Test
    @DisplayName("제대로 post 목록이 불러와지는가")
    void canGetPosts() {
        //given

        //when
        Slice<Post> posts = postQueryService.getPosts(0L, 10);

        //then
    }

    @Test
    @DisplayName("제대로 post 상세 조회가 되는가")
    void canGetPost() {
        //given

        //when
        postQueryService.getPost();

        //then
    }

    @Test
    @DisplayName("제대로 좋아요 누른 post 목록 조회가 되는가")
    void canLikePosts() {
        //given

        //when
        postQueryService.getMyLikePost(0L, 10);

        //then
    }

    @Test
    @DisplayName("내가 쓴 post 목록 조회가 되는가")
    void canMyPosts() {
        //given

        //when
        postQueryService.getMyPost(0L, 10);

        //then
    }

    @Test
    @DisplayName("테스트용 postQuery가 builder 되는가")
    void canPostQueryBuilder() {
        //given

        //when
        PostQueryServiceImpl.builder().build();

        //then
    }
}