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
import com.example.harumeonglog.domain.post.entity.PostReport;
import com.example.harumeonglog.domain.post.entity.enums.PostCategory;
import com.example.harumeonglog.domain.post.repository.PostImageRepository;
import com.example.harumeonglog.domain.post.repository.PostLikeRepository;
import com.example.harumeonglog.domain.post.repository.PostReportRepository;
import com.example.harumeonglog.domain.post.repository.PostRepository;
import com.example.harumeonglog.global.error.exception.PostException;
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
    @Autowired
    private PostReportRepository postReportRepository;

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
        postReportRepository.deleteAll();
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

    @Transactional
    @Test
    @DisplayName("게시글 수정이 잘 되는가")
    void updatePostTest() {
        // given
        Post rawPost = Post.builder()
                .title("title")
                .content("content")
                .member(this.member)
                .category(PostCategory.INFO)
                .build();

        PostImage postImage = PostImage.builder()
                        .postImageKeyName("testImage1")
                        .build();

        rawPost.addPostImage(postImage);

        Post savedPost = postRepository.save(rawPost);

        List<String> updateTestImageList = List.of("updateTestImage1");

        PostRequest.PostUpdateRequest postUpdateRequest = PostRequest.PostUpdateRequest.builder()
                .title("updateTitle")
                .content("updateContent")
                .postCategory(PostCategory.ETC)
                .postImageList(updateTestImageList)
                .build();


        // when
        postCommandService.updatePost(savedPost.getId(), postUpdateRequest, this.member);

        // then
        List<Post> posts = postRepository.findAll();
        assertThat(posts.size()).isEqualTo(1);

        // post 관련
        Post post = posts.get(0);
        assertThat(post.getTitle()).isEqualTo("updateTitle");
        assertThat(post.getContent()).isEqualTo("updateContent");
        assertThat(post.getCategory()).isEqualTo(PostCategory.ETC);
        assertThat(post.getPostReportNum()).isEqualTo(0L);
        assertThat(post.getPostLikeNum()).isEqualTo(0L);
        assertThat(post.getCommentNum()).isEqualTo(0L);
        assertThat(post.getDeletedAt()).isNull();
        assertThat(post.getPostImageList().size()).isEqualTo(updateTestImageList.size());
        assertThat(post.getPostImageList()).extracting("postImageKeyName")
                .containsExactly("updateTestImage1");

        // member 관련 테스트
        Member member = post.getMember();
        assertThat(member.getEmail()).isEqualTo(TEST_EMAIL);
        assertThat(member.getNickname()).isEqualTo(TEST_NICKNAME);
        assertThat(member.getProviderId()).isEqualTo(TEST_PROVIDERID);
        assertThat(member.getSocialType()).isEqualTo(TEST_SOCIALTYPE);
    }

    @Test
    @DisplayName("게시글 수정 시 없는 존재하지 않는 게시글이면 에러가 잘 나나")
    void updatePostPostNullTest() {
        // given
        List<String> updateTestImageList = List.of("updateTestImage1");

        PostRequest.PostUpdateRequest postUpdateRequest = PostRequest.PostUpdateRequest.builder()
                .title("updateTitle")
                .content("updateContent")
                .postCategory(PostCategory.ETC)
                .postImageList(updateTestImageList)
                .build();

        // when + then
        String message = assertThrows(PostException.class, () -> postCommandService.updatePost(2L, postUpdateRequest, this.member)).getMessage();
        assertThat(message).isEqualTo("게시물을 찾지 못했습니다.");
    }

    @Test
    @DisplayName("게시글 수정 시 자신이 아닌 게시글이면 에러가 잘 나나")
    void updatePostPostNotOwnTest() {
        // given
        Member other = memberRepository.save(Member.builder()
                .email(this.TEST_EMAIL)
                .nickname(this.TEST_NICKNAME)
                .providerId(this.TEST_PROVIDERID)
                .socialType(this.TEST_SOCIALTYPE)
                .build());

        Post post = postRepository.save(Post.builder()
                .title("title")
                .content("content")
                .member(member)
                .category(PostCategory.INFO)
                .build()
        );

        List<String> updateTestImageList = List.of("updateTestImage1");

        PostRequest.PostUpdateRequest postUpdateRequest = PostRequest.PostUpdateRequest.builder()
                .title("updateTitle")
                .content("updateContent")
                .postCategory(PostCategory.ETC)
                .postImageList(updateTestImageList)
                .build();

        // when + then
        String message = assertThrows(PostException.class, () -> postCommandService.updatePost(post.getId(), postUpdateRequest, other)).getMessage();
        assertThat(message).isEqualTo("자신의 게시물이 아닙니다.");
    }

    @Test
    @DisplayName("게시글이 잘 삭제 되는가")
    void deletePostTest() {
        // given
        Post rawPost = Post.builder()
                .title("title")
                .content("content")
                .member(this.member)
                .category(PostCategory.INFO)
                .build();

        PostImage postImage = PostImage.builder()
                .postImageKeyName("testImage1")
                .build();

        rawPost.addPostImage(postImage);

        Post savedPost = postRepository.save(rawPost);

        // when
        postCommandService.deletePost(savedPost.getId(), this.member);

        // then
        List<Post> posts = postRepository.findAll();
        assertThat(posts.size()).isEqualTo(1);
        Post post = posts.get(0);
        assertThat(post.getDeletedAt()).isNotNull();
    }

    @Test
    @DisplayName("게시글 삭제 시 없는 존재하지 않는 게시글이면 에러가 잘 나나")
    void deletePostPostNullTest() {
        // given

        // when + then
        log.info(postRepository.findAll().toString());
        String message = assertThrows(PostException.class, () -> postCommandService.deletePost(1L, this.member)).getMessage();
        assertThat(message).isEqualTo("게시물을 찾지 못했습니다.");
    }

    @Test
    @DisplayName("게시글 삭제 시 자신이 아닌 게시글이면 에러가 잘 나나")
    void deletePostPostNotOwnTest() {
        // given
        Member other = memberRepository.save(Member.builder()
                .email(this.TEST_EMAIL)
                .nickname(this.TEST_NICKNAME)
                .providerId(this.TEST_PROVIDERID)
                .socialType(this.TEST_SOCIALTYPE)
                .build());

        Post post = postRepository.save(Post.builder()
                .title("title")
                .content("content")
                .member(member)
                .category(PostCategory.INFO)
                .build()
        );

        // when + then
        String message = assertThrows(PostException.class, () -> postCommandService.deletePost(post.getId(), other)).getMessage();
        assertThat(message).isEqualTo("자신의 게시물이 아닙니다.");
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
    @DisplayName("게시글 좋아요 시 없는 존재하지 않는 게시글이면 에러가 잘 나나")
    void likePostPostNullTest() {
        // given

        // when + then
        String message = assertThrows(PostException.class, () -> postCommandService.likePost(1L, this.member)).getMessage();
        assertThat(message).isEqualTo("게시물을 찾지 못했습니다.");
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
    @DisplayName("게시글 신고 수가 정상적으로 증가한다")
    void postReportTest() {
        // given
        Post post = postRepository.save(
                Post.builder()
                        .category(PostCategory.INFO)
                        .content("내용")
                        .title("제목")
                        .member(member)
                        .postReportNum(0L)
                        .build()
        );

        // when
        postCommandService.reportPost(post.getId(), member);

        // then
        Post updatedPost = postRepository.findById(post.getId()).orElseThrow();
        assertEquals(1L, updatedPost.getPostReportNum());

        log.info("최종 좋아요 수: " + updatedPost.getPostReportNum());
    }

    @Test
    @DisplayName("게시글 신고 수가 정상적으로 감소한다")
    void postUnReportTest() {
        // given
        Post post = postRepository.save(
                Post.builder()
                        .category(PostCategory.INFO)
                        .content("내용")
                        .title("제목")
                        .member(member)
                        .postReportNum(1L)
                        .build()
        );

        postReportRepository.save(
                PostReport.builder()
                        .member(member)
                        .post(post)
                        .build()
        );

        // when
        postCommandService.reportPost(post.getId(), member);

        // then
        Post updatedPost = postRepository.findById(post.getId()).orElseThrow();
        assertEquals(0L, updatedPost.getPostReportNum());
    }

    @Test
    @DisplayName("게시글 신고 시 없는 존재하지 않는 게시글이면 에러가 잘 나나")
    void reportPostPostNullTest() {
        // given

        // when + then
        String message = assertThrows(PostException.class, () -> postCommandService.reportPost(1L, this.member)).getMessage();
        assertThat(message).isEqualTo("게시물을 찾지 못했습니다.");
    }

    @Test
    @DisplayName("한 명이 한번에 여러 번의 신고를 눌렀을 때 원자적으로 처리가 되는가")
    void concurrencyReportTest() throws InterruptedException {
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
                    postCommandService.reportPost(savedPost.getId(), member);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        // then
        Post updatedPost = postRepository.findById(post.getId()).orElseThrow();
        log.info("최종 신고 수: " + updatedPost.getPostReportNum());

        // 신고 수가 0 또는 1이 아니면 동시성 문제 발생 가능
        assertTrue(
                updatedPost.getPostReportNum() == 0L || updatedPost.getPostReportNum() == 1L,
                "신고 수가 비정상적으로 계산되었습니다."
        );
    }

    @Test
    @DisplayName("여러 명이 동시에 하나의 게시글을 신고할 때 동시성 문제 없이 정확히 처리되는가")
    void concurrencyMultiMemberReportTest() throws InterruptedException {
        // given
        Post post = postRepository.save(
                Post.builder()
                        .category(PostCategory.INFO)
                        .content("내용")
                        .title("제목")
                        .member(member)
                        .postReportNum(0L)
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
                    postCommandService.reportPost(post.getId(), members.get(idx));
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        // then
        Post updatedPost = postRepository.findById(post.getId()).orElseThrow();
        log.info("최종 신고 수: " + updatedPost.getPostReportNum());

        assertEquals(threadCount, updatedPost.getPostReportNum());
    }

}