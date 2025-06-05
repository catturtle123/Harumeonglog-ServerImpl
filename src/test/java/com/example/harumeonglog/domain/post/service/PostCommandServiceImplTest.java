package com.example.harumeonglog.domain.post.service;

import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.member.entity.enums.SocialType;
import com.example.harumeonglog.domain.member.repository.MemberRepository;
import com.example.harumeonglog.domain.post.entity.Post;
import com.example.harumeonglog.domain.post.entity.enums.PostCategory;
import com.example.harumeonglog.domain.post.repository.PostRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

    @Test
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
  
}