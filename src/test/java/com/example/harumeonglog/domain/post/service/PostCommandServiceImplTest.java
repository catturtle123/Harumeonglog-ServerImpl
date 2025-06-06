package com.example.harumeonglog.domain.post.service;

import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.member.entity.enums.SocialType;
import com.example.harumeonglog.domain.member.repository.MemberRepository;
import com.example.harumeonglog.domain.post.entity.Post;
import com.example.harumeonglog.domain.post.entity.PostLike;
import com.example.harumeonglog.domain.post.entity.enums.PostCategory;
import com.example.harumeonglog.domain.post.repository.PostLikeRepository;
import com.example.harumeonglog.domain.post.repository.PostRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class PostCommandServiceImplTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PostCommandService postCommandService;
    @Autowired
    private PostLikeRepository postLikeRepository;

    @Test
    @DisplayName("게시글 좋아요 수가 정상적으로 증가한다")
    void postLikeTest() {
        // given
        Member member = memberRepository.save(
                Member.builder()
                        .email("example@example.com")
                        .nickname("example")
                        .providerId("example")
                        .socialType(SocialType.KAKAO)
                        .build()
        );

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

        // 로그 확인용
        System.out.println("최종 좋아요 수: " + updatedPost.getPostLikeNum());
    }

    @Test
    @DisplayName("게시글 좋아요 수가 정상적으로 감소한다")
    void postUnLikeTest() {
        // given
        Member member = memberRepository.save(
                Member.builder()
                        .email("example@example.com")
                        .nickname("example")
                        .providerId("example")
                        .socialType(SocialType.KAKAO)
                        .build()
        );

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
        Member member = Member.builder()
                .email("example@example.com")
                .nickname("example")
                .providerId("example")
                .socialType(SocialType.KAKAO).build();

        Member savedMember = memberRepository.save(member);

        Post post = Post.builder()
                .category(PostCategory.INFO)
                .content("내용")
                .title("제목")
                .member(savedMember)
                .build();

        Post savedPost = postRepository.save(post);

        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(threadCount);

        // when
        for (int i = 0; i < threadCount; i++) {
            executorService.execute(() -> {
                try {
                    postCommandService.likePost(savedPost.getId(), savedMember);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        // then
        Post updatedPost = postRepository.findById(post.getId()).orElseThrow();
        System.out.println("최종 좋아요 수: " + updatedPost.getPostLikeNum());

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
        Member writer = Member.builder()
                .email("example@example.com")
                .nickname("example")
                .providerId("example")
                .socialType(SocialType.KAKAO).build();

        memberRepository.save(writer);

        Post post = postRepository.save(
                Post.builder()
                        .category(PostCategory.INFO)
                        .content("내용")
                        .title("제목")
                        .member(writer)
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
        System.out.println("최종 좋아요 수: " + updatedPost.getPostLikeNum());

        assertEquals(threadCount, updatedPost.getPostLikeNum());
    }


}