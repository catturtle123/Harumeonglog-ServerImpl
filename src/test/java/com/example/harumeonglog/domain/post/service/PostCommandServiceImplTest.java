package com.example.harumeonglog.domain.post.service;

import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.member.entity.enums.SocialType;
import com.example.harumeonglog.domain.member.repository.MemberRepository;
import com.example.harumeonglog.domain.post.dto.request.PostRequest;
import com.example.harumeonglog.domain.post.dto.response.PostResponse;
import com.example.harumeonglog.domain.post.entity.Post;
import com.example.harumeonglog.domain.post.entity.PostImage;
import com.example.harumeonglog.domain.post.entity.PostLike;
import com.example.harumeonglog.domain.post.entity.enums.PostCategory;
import com.example.harumeonglog.domain.post.repository.PostImageRepository;
import com.example.harumeonglog.domain.post.repository.PostLikeRepository;
import com.example.harumeonglog.domain.post.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class PostCommandServiceImplTest {

    private static final Logger log = LoggerFactory.getLogger(PostCommandServiceImplTest.class);

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PostCommandService postCommandService;
    @Autowired
    private PostLikeRepository postLikeRepository;
    @Autowired
    private PostImageRepository postImageRepository;

    private Member member;
    private final String email = "example@example.com";
    private final String nickname = "example";
    private final String providerId = "example";
    private final SocialType socialType = SocialType.KAKAO;

    @BeforeEach
    void setup() {
        this.member = memberRepository.save(Member.builder()
                    .email(this.email)
                    .nickname(this.nickname)
                    .providerId(this.providerId)
                    .socialType(this.socialType)
                    .build());
    }

    @Test
    @DisplayName("게시글 좋아요 수가 정상적으로 증가한다")
    void postLikeTest() {
        // given
        Post post = postRepository.save(
                Post.builder()
                        .category(PostCategory.INFO)
                        .content("내용")
                        .title("제목")
                        .member(member)
                        .postLikeNum(0L)
                        .build()
        );

        // when
        postCommandService.likePost(post.getId(), member);

        // then
        Post updatedPost = postRepository.findById(post.getId()).orElseThrow();
        assertEquals(1L, updatedPost.getPostLikeNum());

        log.info("최종 좋아요 수: " + updatedPost.getPostLikeNum());
    }

    @Test
    @DisplayName("게시글 좋아요 수가 정상적으로 감소한다")
    void postUnLikeTest() {
        // given
        Post post = postRepository.save(
                Post.builder()
                        .category(PostCategory.INFO)
                        .content("내용")
                        .title("제목")
                        .member(member)
                        .postLikeNum(1L)
                        .build()
        );

        postLikeRepository.save(
                PostLike.builder()
                        .member(member)
                        .post(post)
                        .build()
        );

        // when
        postCommandService.likePost(post.getId(), member);

        // then
        Post updatedPost = postRepository.findById(post.getId()).orElseThrow();
        assertEquals(0L, updatedPost.getPostLikeNum());
    }

    @Test
    @DisplayName("한 명이 한번에 여러 번의 좋아요를 눌렀을 때 원자적으로 처리가 되는가")
    void concurrencyLikeTest() throws InterruptedException {
        // given
        Post post = Post.builder()
                .category(PostCategory.INFO)
                .content("내용")
                .title("제목")
                .member(member)
                .build();

        Post savedPost = postRepository.save(post);

        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(threadCount);

        // when
        for (int i = 0; i < threadCount; i++) {
            executorService.execute(() -> {
                try {
                    postCommandService.likePost(savedPost.getId(), member);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        // then
        Post updatedPost = postRepository.findById(post.getId()).orElseThrow();
        log.info("최종 좋아요 수: " + updatedPost.getPostLikeNum());

        // 좋아요 수가 0 또는 1이 아니면 동시성 문제 발생 가능
        assertTrue(
                updatedPost.getPostLikeNum() == 0L || updatedPost.getPostLikeNum() == 1L,
                "좋아요 수가 비정상적으로 계산되었습니다."
        );
    }

    @Test
    @DisplayName("여러 명이 동시에 하나의 게시글을 좋아요할 때 동시성 문제 없이 정확히 처리되는가")
    void concurrencyMultiMemberLikeTest() throws InterruptedException {
        // given
        Post post = postRepository.save(
                Post.builder()
                        .category(PostCategory.INFO)
                        .content("내용")
                        .title("제목")
                        .member(member)
                        .postLikeNum(0L)
                        .build()
        );

        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(threadCount);

        List<Member> members = IntStream.range(0, threadCount)
                .mapToObj(i -> Member.builder()
                        .email("user" + i + "@test.com")
                        .nickname("user" + i)
                        .providerId("provider" + i)
                        .socialType(SocialType.KAKAO)
                        .build())
                .map(memberRepository::save)
                .collect(Collectors.toList());

        // when
        for (int i = 0; i < threadCount; i++) {
            final int idx = i;
            executorService.execute(() -> {
                try {
                    postCommandService.likePost(post.getId(), members.get(idx));
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        // then
        Post updatedPost = postRepository.findById(post.getId()).orElseThrow();
        log.info("최종 좋아요 수: " + updatedPost.getPostLikeNum());

        assertEquals(threadCount, updatedPost.getPostLikeNum());
    }

    @Test
    @DisplayName("게시물 생성이 잘 되는가")
    @Transactional
    void isPostCreate() {
        // given
        List<String> imageKeys = List.of("test1", "test2");

        PostRequest.PostCreateRequest postCreateRequest = PostRequest.PostCreateRequest.builder()
                .postCategory(PostCategory.INFO)
                .postImageList(imageKeys)
                .content("내용")
                .title("제목")
                .build();

        // when
        PostResponse.PostCreateResponse response = postCommandService.createPost(postCreateRequest, member);

        // then
        Post savedPost = postRepository.findById(response.getPostId())
                .orElseThrow(() -> new AssertionError("Post가 저장되지 않았습니다."));

        assertNotNull(savedPost.getId(), "게시물 ID는 null이 아니어야 합니다.");
        assertEquals("제목", savedPost.getTitle());
        assertEquals("내용", savedPost.getContent());
        assertEquals(PostCategory.INFO, savedPost.getCategory());
        assertEquals(0, savedPost.getPostLikeNum());
        assertEquals(0, savedPost.getCommentNum());
        assertEquals(0, savedPost.getPostReportNum());
        assertNull(savedPost.getDeletedAt(), "deletedAt은 null이어야 합니다.");

        List<PostImage> postImageList = postImageRepository.findAllByPost(savedPost);

        List<String> savedImageKeys = postImageList
                .stream()
                .map(PostImage::getPostImageKeyName)
                .toList();

        assertEquals(imageKeys.size(), savedImageKeys.size(), "이미지 개수가 일치해야 합니다.");
        assertTrue(savedImageKeys.containsAll(imageKeys), "모든 이미지 키가 저장되어야 합니다.");
    }

}