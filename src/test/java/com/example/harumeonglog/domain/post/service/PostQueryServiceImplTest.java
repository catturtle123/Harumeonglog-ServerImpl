package com.example.harumeonglog.domain.post.service;

import com.example.harumeonglog.domain.member.dto.response.MemberResponse;
import com.example.harumeonglog.domain.member.dto.response.MemberResponse.MemberInfoResponse;
import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.member.entity.enums.SocialType;
import com.example.harumeonglog.domain.member.repository.MemberRepository;
import com.example.harumeonglog.domain.post.controller.enums.PostRequestCategory;
import com.example.harumeonglog.domain.post.dto.request.PostRequest;
import com.example.harumeonglog.domain.post.dto.response.PostResponse;
import com.example.harumeonglog.domain.post.dto.response.PostResponse.PostDetailResponse;
import com.example.harumeonglog.domain.post.dto.response.PostResponse.PostPreviewListResponse;
import com.example.harumeonglog.domain.post.dto.response.PostResponse.PostPreviewResponse;
import com.example.harumeonglog.domain.post.entity.Post;
import com.example.harumeonglog.domain.post.entity.PostImage;
import com.example.harumeonglog.domain.post.entity.enums.PostCategory;
import com.example.harumeonglog.domain.post.repository.PostLikeRepository;
import com.example.harumeonglog.domain.post.repository.PostRepository;
import com.example.harumeonglog.global.util.S3Util;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.as;
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
class PostQueryServiceImplTest {

    private static final Logger log = LoggerFactory.getLogger(PostQueryServiceImplTest.class);

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostQueryService postQueryService;

    @Autowired
    private PostLikeRepository postLikeRepository;

    @Autowired
    private MemberRepository memberRepository;

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
    void clear() {
        postLikeRepository.deleteAll();
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("게시물 검색이 정상 작동하는가")
    void getPostsTest() {
        // given
        PostCategory[] categories = PostCategory.values();

        IntStream.range(0, 100).forEach(i -> {
            PostImage image = PostImage.builder()
                    .postImageKeyName("testImage" + i)
                    .build();

            Post post = Post.builder()
                    .title("post" + i + "title")
                    .content("post" + i + "content")
                    .member(member)
                    .category(categories[i / 20])
                    .build();

            post.addPostImage(image);
            postRepository.save(post);
        });

        Long cursor = 0L;
        Integer size = 20;
        String name = "post";
        PostRequestCategory postCategory = PostRequestCategory.ETC;

        // when
        PostPreviewListResponse response =
                postQueryService.getPosts(cursor, size, name, postCategory, member);

        // then
        assertEquals(size, response.getItems().size());
        assertTrue(response.getItems().stream()
                .allMatch(p -> p.getPostCategory().name().equals(postCategory.name())));
        assertTrue(response.getItems().stream()
                .allMatch(p -> p.getContent().contains(name) || p.getTitle().contains(name)));
    }

    @Test
    @DisplayName("게시물 단건 조회 잘 되는가")
    void getPostsDefaultTest() {
        // given
        PostImage postImage = PostImage.builder()
                .postImageKeyName("testImage")
                .build();

        Post post = Post.builder()
                    .title("post")
                    .content("content")
                    .member(member)
                    .category(PostCategory.INFO)
                    .build();

       post.addPostImage(postImage);
        Post savedPost = postRepository.save(post);

        // when
        PostDetailResponse postDetailResponse = postQueryService.getPost(member, savedPost.getId());

        // then

        // post
        assertThat(postDetailResponse.getPostId()).isEqualTo(savedPost.getId());
        assertThat(postDetailResponse.getTitle()).isEqualTo(savedPost.getTitle());
        assertThat(postDetailResponse.getContent()).isEqualTo(savedPost.getContent());
        assertThat(postDetailResponse.getIsLiked()).isFalse();
        assertThat(postDetailResponse.getPostCategory()).isEqualTo(savedPost.getCategory());
        assertThat(postDetailResponse.getCreatedAt()).isEqualTo(savedPost.getCreatedAt());
        assertThat(postDetailResponse.getCommentNum()).isEqualTo(savedPost.getCommentNum());
        assertThat(postDetailResponse.getLikeNum()).isEqualTo(savedPost.getPostLikeNum());
        assertThat(post.getPostImageList()).extracting("postImageKeyName")
                .containsExactly("testImage");

        // member
        MemberInfoResponse memberInfoResponse = postDetailResponse.getMemberInfoResponse();
        assertThat(memberInfoResponse.getMemberId()).isEqualTo(member.getId());
        assertThat(memberInfoResponse.getEmail()).isEqualTo(member.getEmail());
        assertThat(memberInfoResponse.getNickname()).isEqualTo(member.getNickname());
        assertThat(memberInfoResponse.getImage()).isEqualTo(member.getImage());
    }

    @Test
    @DisplayName("내 게시물 조회가 잘 되는가")
    void getMyPostTest() {
        // given
        PostCategory[] categories = PostCategory.values();

        IntStream.range(0, 20).forEach(i -> {
            PostImage image = PostImage.builder()
                    .postImageKeyName("testImage" + i)
                    .build();

            Post post = Post.builder()
                    .title("post" + i + "title")
                    .content("post" + i + "content")
                    .member(member)
                    .category(categories[i / 4])
                    .build();

            post.addPostImage(image);
            postRepository.save(post);
        });

        Long cursor = 0L;
        Integer size = 20;

        // when
        PostPreviewListResponse response = postQueryService.getMyPost(cursor, size, member);

        // then
        assertEquals(size, response.getItems().size());

        List<PostPreviewResponse> items = response.getItems();

        IntStream.range(0, items.size()).forEach(i -> {
            PostPreviewResponse item = items.get(i);
            int expectedIndex = items.size() - 1 - i;

            assertEquals(member.getId(), item.getMemberInfoResponse().getMemberId());
            assertEquals(item.getPostId(), expectedIndex + 1);
            assertTrue(item.getTitle().contains("post" + expectedIndex));
            assertTrue(item.getContent().contains("post" + expectedIndex));
        });
    }

    @Test
    @DisplayName("내 게시물 조회 시 다른 사람의 게시물은 조회를 잘 막는가")
    void getMyPostAnotherPostTest() {
        // given
        PostImage postImage = PostImage.builder()
                .postImageKeyName("testImage")
                .build();

        Post post = Post.builder()
                .title("post")
                .content("content")
                .member(member)
                .category(PostCategory.INFO)
                .build();

        post.addPostImage(postImage);
        Post myPost = postRepository.save(post);

        Member other = memberRepository.save(Member.builder()
                .email(this.TEST_EMAIL)
                .nickname(this.TEST_NICKNAME)
                .providerId(this.TEST_PROVIDERID)
                .socialType(this.TEST_SOCIALTYPE)
                .build());

        PostImage otherPostImage = PostImage.builder()
                .postImageKeyName("otherImage")
                .build();

        Post otherRawPost = Post.builder()
                .title("otherPost")
                .content("otherContent")
                .member(other)
                .category(PostCategory.INFO)
                .build();

        otherRawPost.addPostImage(otherPostImage);
        postRepository.save(otherRawPost);

        Long cursor = 0L;
        Integer size = 20;

        // then
        PostPreviewListResponse myPostResponse = postQueryService.getMyPost(cursor, size, member);

        // when
        assertThat(myPostResponse.getItems().size()).isEqualTo(1);
        PostPreviewResponse postPreviewResponse = myPostResponse.getItems().get(0);

        // post
        assertThat(postPreviewResponse.getPostId()).isEqualTo(myPost.getId());
        assertThat(postPreviewResponse.getTitle()).isEqualTo(myPost.getTitle());
        assertThat(postPreviewResponse.getContent()).isEqualTo(myPost.getContent());
        assertThat(postPreviewResponse.getIsLiked()).isFalse();
        assertThat(postPreviewResponse.getPostCategory()).isEqualTo(myPost.getCategory());
        assertThat(postPreviewResponse.getCreatedAt()).isEqualTo(myPost.getCreatedAt());
        assertThat(postPreviewResponse.getCommentNum()).isEqualTo(myPost.getCommentNum());
        assertThat(postPreviewResponse.getLikeNum()).isEqualTo(myPost.getPostLikeNum());
        assertThat(post.getPostImageList()).extracting("postImageKeyName")
                .containsExactly("testImage");

        // member
        MemberInfoResponse memberInfoResponse = postPreviewResponse.getMemberInfoResponse();
        assertThat(memberInfoResponse.getMemberId()).isEqualTo(member.getId());
        assertThat(memberInfoResponse.getEmail()).isEqualTo(member.getEmail());
        assertThat(memberInfoResponse.getNickname()).isEqualTo(member.getNickname());
        assertThat(memberInfoResponse.getImage()).isEqualTo(member.getImage());
    }
}