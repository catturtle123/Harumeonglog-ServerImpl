package com.example.harumeonglog.domain.post.scheduler;

import com.example.harumeonglog.domain.post.dto.response.PostResponse;
import com.example.harumeonglog.domain.post.entity.Post;
import com.example.harumeonglog.domain.post.repository.PostRepository;
import com.example.harumeonglog.domain.post.converter.PostConverter;
import com.example.harumeonglog.global.util.CacheKeyUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 어제 인기 게시물 캐시를 위한 Eager Preloading 스케줄러
 * 매일 새벽 3시에 캐시를 미리 적재하여 캐시 미스로 인한 DB 부하를 방지
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class PostCacheScheduler {

    private final PostRepository postRepository;
    private final CacheManager yesterdayPostsCacheManager;

    @Scheduled(cron = "0 0 3 * * ?") // 매일 새벽 3시 실행
    public void preloadYesterdayGoodPostsCache() {
        String cacheKey = CacheKeyUtil.getYesterdayPostsCacheKey(); // 통일된 키 생성 메서드 사용
        log.info("어제 인기 게시물 캐시 Eager Preloading 시작: {} - 캐시키: {}", LocalDateTime.now(), cacheKey);
        
        try {
            // 1. DB에서 데이터 조회 (새벽 3시 기준 어제 날짜)
            LocalDate yesterday = CacheKeyUtil.getYesterdayDate();
            List<Post> yesterdayTopPosts = postRepository.findTop5PostsByDateAndLikes(yesterday, PageRequest.of(0, 5));
            PostResponse.PostYesterdayResponseList response = PostConverter.toPostYesterdayResponseList(yesterdayTopPosts);
            
            // 2. CacheManager를 통해 명시적으로 캐시에 적재
            Cache cache = yesterdayPostsCacheManager.getCache("getYesterdayGoodPosts");
            if (cache != null) {
                cache.put(cacheKey, response);
                log.info("어제 인기 게시물 캐시 Eager Preloading 성공: {} - 캐시키: {} - 데이터 건수: {}", 
                        LocalDateTime.now(), cacheKey, yesterdayTopPosts.size());
            } else {
                log.warn("캐시를 찾을 수 없음: getYesterdayGoodPosts");
            }
        } catch (Exception e) {
            log.error("어제 인기 게시물 캐시 Eager Preloading 실패: {} - 캐시키: {}", e.getMessage(), cacheKey, e);
            // 실패하더라도 Cache Aside 패턴으로 인해 첫 요청 시 자동으로 캐시 적재됨
        }
    }
}
