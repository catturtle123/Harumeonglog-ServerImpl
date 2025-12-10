package com.example.harumeonglog.global.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 캐시 키 생성을 위한 유틸리티 클래스
 * 날짜 기반 캐시 키 계산 로직을 중앙화하여 일관성과 테스트 가능성 확보
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CacheKeyUtil {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final int CACHE_REBUILD_HOUR = 3; // 새벽 3시 기준

    /**
     * 어제 인기 게시물 캐시를 위한 날짜 키 생성
     * 
     * 새벽 3시를 기준으로 "어제"를 계산:
     * - 00:00 ~ 02:59: 전전날 날짜 (새벽 3시에 생성된 캐시를 계속 사용)
     * - 03:00 ~ 23:59: 어제 날짜 (새롭게 리빌드된 캐시 사용)
     * 
     * 예시:
     * - 2025-12-10 00:30 → "2025-12-08" (12월 9일 새벽 3시에 생성된 캐시)
     * - 2025-12-10 03:00 → "2025-12-09" (12월 10일 새벽 3시에 생성된 캐시)
     * - 2025-12-10 15:00 → "2025-12-09" (동일 캐시)
     * 
     * @return yyyy-MM-dd 형식의 날짜 문자열
     */
    public static String getYesterdayPostsCacheDate() {
        LocalDateTime now = LocalDateTime.now();
        LocalDate today = now.toLocalDate();
        int currentHour = now.getHour();
        
        // 새벽 3시 이전이면 전전날, 이후면 어제
        LocalDate targetDate = (currentHour < CACHE_REBUILD_HOUR) 
                ? today.minusDays(2) 
                : today.minusDays(1);
        
        return targetDate.format(DATE_FORMATTER);
    }

    /**
     * 새벽 3시 기준으로 어제 날짜를 LocalDate로 반환
     * DB 쿼리에 사용하기 위한 메서드
     * 
     * @return 새벽 3시 기준 어제 날짜 (LocalDate)
     */
    public static LocalDate getYesterdayDate() {
        LocalDateTime now = LocalDateTime.now();
        LocalDate today = now.toLocalDate();
        int currentHour = now.getHour();
        
        // 새벽 3시 이전이면 전전날, 이후면 어제
        return (currentHour < CACHE_REBUILD_HOUR) 
                ? today.minusDays(2) 
                : today.minusDays(1);
    }

    /**
     * 어제 인기 게시물 전체 캐시 키 생성
     * 
     * ⚠️ 중요: 이 메서드는 @Cacheable과 스케줄러 양쪽에서 모두 사용됩니다.
     * 키 생성 로직을 절대 분리하지 마세요. 1:1 일치가 보장되어야 합니다.
     * 
     * @return "posts:yesterday:yyyy-MM-dd" 형식의 캐시 키
     */
    public static String getYesterdayPostsCacheKey() {
        return "posts:yesterday:" + getYesterdayPostsCacheDate();
    }
}
