package com.example.harumeonglog.domain.post.scheduler;

import com.example.harumeonglog.domain.post.converter.PostConverter;
import com.example.harumeonglog.domain.post.dto.response.PostResponse;
import com.example.harumeonglog.domain.post.entity.Post;
import com.example.harumeonglog.domain.post.entity.enums.PostCategory;
import com.example.harumeonglog.domain.post.repository.PostRepository;
import com.example.harumeonglog.global.util.CacheKeyUtil;
import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.member.entity.enums.SocialType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

/**
 * PostCacheScheduler 테스트
 * 
 * 핵심 검증 사항:
 * 1. 스케줄러가 올바른 캐시 키를 사용하는지
 * 2. CacheManager를 통해 캐시에 명시적으로 적재하는지
 * 3. 예외 발생 시에도 안전하게 처리하는지
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("PostCacheScheduler 테스트")
class PostCacheSchedulerTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private CacheManager yesterdayPostsCacheManager;

    @Mock
    private Cache cache;

    @InjectMocks
    private PostCacheScheduler postCacheScheduler;

    private List<Post> mockPosts;
    private Member mockMember;

    @BeforeEach
    void setUp() {
        mockMember = Member.builder()
                .id(1L)
                .nickname("테스트유저")
                .socialType(SocialType.KAKAO)
                .build();

        mockPosts = createMockPosts();
    }

    @Test
    @DisplayName("스케줄러는 CacheKeyUtil과 동일한 키를 사용하여 캐시에 적재해야 한다")
    void shouldUseSameCacheKeyAsCacheKeyUtil() {
        // given
        String expectedCacheKey = CacheKeyUtil.getYesterdayPostsCacheKey();
        given(postRepository.findTop5PostsByDateAndLikes(any(LocalDate.class), any(Pageable.class))).willReturn(mockPosts);
        given(yesterdayPostsCacheManager.getCache("getYesterdayGoodPosts")).willReturn(cache);

        // when
        postCacheScheduler.preloadYesterdayGoodPostsCache();

        // then
        verify(cache).put(eq(expectedCacheKey), any(PostResponse.PostYesterdayResponseList.class));
    }

    @Test
    @DisplayName("스케줄러는 DB에서 데이터를 조회하고 캐시에 저장해야 한다")
    void shouldLoadDataFromDBAndPutToCache() {
        // given
        given(postRepository.findTop5PostsByDateAndLikes(any(LocalDate.class), any(Pageable.class))).willReturn(mockPosts);
        given(yesterdayPostsCacheManager.getCache("getYesterdayGoodPosts")).willReturn(cache);

        // when
        postCacheScheduler.preloadYesterdayGoodPostsCache();

        // then
        verify(postRepository, times(1)).findTop5PostsByDateAndLikes(any(LocalDate.class), any(Pageable.class));
        verify(cache, times(1)).put(anyString(), any(PostResponse.PostYesterdayResponseList.class));
    }

    @Test
    @DisplayName("캐시에 저장되는 데이터는 올바른 형식이어야 한다")
    void shouldPutCorrectDataFormatToCache() {
        // given
        given(postRepository.findTop5PostsByDateAndLikes(any(LocalDate.class), any(Pageable.class))).willReturn(mockPosts);
        given(yesterdayPostsCacheManager.getCache("getYesterdayGoodPosts")).willReturn(cache);

        // when
        postCacheScheduler.preloadYesterdayGoodPostsCache();

        // then
        verify(cache).put(anyString(), argThat(data -> {
            if (data instanceof PostResponse.PostYesterdayResponseList) {
                PostResponse.PostYesterdayResponseList response = 
                        (PostResponse.PostYesterdayResponseList) data;
                return response.getItems() != null && 
                       response.getItems().size() == mockPosts.size();
            }
            return false;
        }));
    }

    @Test
    @DisplayName("캐시가 없을 경우 예외를 발생시키지 않고 경고 로그만 남겨야 한다")
    void shouldNotThrowExceptionWhenCacheIsNull() {
        // given
        given(postRepository.findTop5PostsByDateAndLikes(any(LocalDate.class), any(Pageable.class))).willReturn(mockPosts);
        given(yesterdayPostsCacheManager.getCache("getYesterdayGoodPosts")).willReturn(null);

        // when & then - 예외가 발생하지 않아야 함
        postCacheScheduler.preloadYesterdayGoodPostsCache();

        verify(cache, never()).put(anyString(), any());
    }

    @Test
    @DisplayName("DB 조회 실패 시 예외를 발생시키지 않고 에러 로그만 남겨야 한다")
    void shouldNotThrowExceptionWhenDBQueryFails() {
        // given
        given(postRepository.findTop5PostsByDateAndLikes(any(LocalDate.class), any(Pageable.class)))
                .willThrow(new RuntimeException("DB 연결 오류"));

        // when & then - 예외가 발생하지 않아야 함
        postCacheScheduler.preloadYesterdayGoodPostsCache();

        verify(cache, never()).put(anyString(), any());
    }

    @Test
    @DisplayName("빈 리스트가 반환되어도 정상적으로 캐시에 저장해야 한다")
    void shouldHandleEmptyListCorrectly() {
        // given
        given(postRepository.findTop5PostsByDateAndLikes(any(LocalDate.class), any(Pageable.class))).willReturn(new ArrayList<>());
        given(yesterdayPostsCacheManager.getCache("getYesterdayGoodPosts")).willReturn(cache);

        // when
        postCacheScheduler.preloadYesterdayGoodPostsCache();

        // then
        verify(cache).put(anyString(), argThat(data -> {
            if (data instanceof PostResponse.PostYesterdayResponseList) {
                PostResponse.PostYesterdayResponseList response = 
                        (PostResponse.PostYesterdayResponseList) data;
                return response.getItems() != null && response.getItems().isEmpty();
            }
            return false;
        }));
    }

    @Test
    @DisplayName("스케줄러는 정확히 5개의 게시물을 캐시에 저장해야 한다")
    void shouldCacheExactlyFivePosts() {
        // given
        given(postRepository.findTop5PostsByDateAndLikes(any(LocalDate.class), any(Pageable.class))).willReturn(mockPosts);
        given(yesterdayPostsCacheManager.getCache("getYesterdayGoodPosts")).willReturn(cache);

        // when
        postCacheScheduler.preloadYesterdayGoodPostsCache();

        // then
        verify(cache).put(anyString(), argThat(data -> {
            if (data instanceof PostResponse.PostYesterdayResponseList) {
                PostResponse.PostYesterdayResponseList response = 
                        (PostResponse.PostYesterdayResponseList) data;
                return response.getItems().size() == 5;
            }
            return false;
        }));
    }

    private List<Post> createMockPosts() {
        List<Post> posts = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            Post post = Post.builder()
                    .id((long) i)
                    .title("인기 게시물 " + i)
                    .content("내용 " + i)
                    .category(PostCategory.QNA)
                    .member(mockMember)
                    .build();
            posts.add(post);
        }
        return posts;
    }
}
