package com.example.harumeonglog.domain.post.service;

import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.member.entity.enums.SocialType;
import com.example.harumeonglog.domain.member.repository.MemberRepository;
import com.example.harumeonglog.domain.post.dto.request.PostRequest;
import com.example.harumeonglog.domain.post.dto.response.PostResponse;
import com.example.harumeonglog.domain.post.dto.response.PostResponse.PostCreateResponse;
import com.example.harumeonglog.domain.post.entity.Post;
import com.example.harumeonglog.domain.post.entity.PostImage;
import com.example.harumeonglog.domain.post.entity.PostLike;
import com.example.harumeonglog.domain.post.entity.enums.PostCategory;
import com.example.harumeonglog.domain.post.repository.PostImageRepository;
import com.example.harumeonglog.domain.post.repository.PostLikeRepository;
import com.example.harumeonglog.domain.post.repository.PostRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = {
        "LOCAL_DB_URL=jdbc:h2:mem:testdb",
        "LOCAL_DB_USERNAME=sa",
        "LOCAL_DB_PW=",
        "JWT_SECRET=testjwttestjwttestjwttestjwttestjwttestjwt"
})
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

    private Member member;

    private final String TEST_EMAIL = "example@example.com";
    private final String TEST_NICKNAME = "example";
    private final String TEST_PROVIDERID = "example";
    private final SocialType TEST_SOCIALTYPE = SocialType.KAKAO;

    @BeforeEach
    void setup() {
        this.member = memberRepository.save(Member.builder()
                    .email(this.TEST_EMAIL)
                    .nickname(this.TEST_NICKNAME)
                    .providerId(this.TEST_PROVIDERID)
                    .socialType(this.TEST_SOCIALTYPE)
                    .build());
    }

    @AfterEach
    void clean() {
        postLikeRepository.deleteAll();
        postRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Transactional
    @Test
    @DisplayName("게시글이 잘 생성되는 가")
    void createPostTest() {
        // given
        List<String> postImageList = List.of("testImage1", "testImage2", "testImage3");

        PostRequest.PostCreateRequest postCreateRequest = PostRequest.PostCreateRequest.builder()
                .title("title")
                .content("content")
                .postCategory(PostCategory.INFO)
                .postImageList(postImageList)
                .build();

        // when
        postCommandService.createPost(postCreateRequest, this.member);

        // then
        List<Post> posts = postRepository.findAll();
        assertThat(posts.size()).isEqualTo(1);

        // post 관련 테스트
        Post post = posts.get(0);
        assertThat(post.getTitle()).isEqualTo("title");
        assertThat(post.getContent()).isEqualTo("content");
        assertThat(post.getCategory()).isEqualTo(PostCategory.INFO);
        assertThat(post.getPostReportNum()).isEqualTo(0L);
        assertThat(post.getPostLikeNum()).isEqualTo(0L);
        assertThat(post.getCommentNum()).isEqualTo(0L);
        assertThat(post.getDeletedAt()).isNull();
        assertThat(post.getPostImageList().size()).isEqualTo(postImageList.size());
        assertThat(post.getPostImageList()).extracting("postImageKeyName")
                .containsExactly("testImage1", "testImage2", "testImage3");

        // member 관련 테스트
        Member member = post.getMember();
        assertThat(member.getEmail()).isEqualTo(TEST_EMAIL);
        assertThat(member.getNickname()).isEqualTo(TEST_NICKNAME);
        assertThat(member.getProviderId()).isEqualTo(TEST_PROVIDERID);
        assertThat(member.getSocialType()).isEqualTo(TEST_SOCIALTYPE);
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

}