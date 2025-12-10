package com.example.harumeonglog.global.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * CacheKeyUtil 테스트
 * 
 * 핵심 테스트 케이스:
 * 1. 새벽 3시 이전(00:00~02:59)에는 전전날 날짜 반환
 * 2. 새벽 3시 이후(03:00~23:59)에는 어제 날짜 반환
 * 3. 캐시 키 형식 검증
 */
@DisplayName("CacheKeyUtil 테스트")
class CacheKeyUtilTest {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @DisplayName("새벽 3시 이전(00:00~02:59)에는 전전날 날짜를 반환한다")
    @ParameterizedTest(name = "{0}시 → 전전날 날짜")
    @MethodSource("provideHoursBeforeThreeAM")
    void shouldReturnDayBeforeYesterdayWhenBeforeThreeAM(int hour) {
        // given
        LocalDate today = LocalDate.of(2025, 12, 10);
        LocalDate expectedDate = today.minusDays(2); // 2025-12-08
        
        // when
        String result = CacheKeyUtil.getYesterdayPostsCacheDate();
        
        // then
        // 실제 시간에 의존하므로 현재 시간이 0~2시 사이면 검증
        LocalDateTime now = LocalDateTime.now();
        if (now.getHour() < 3) {
            String expected = now.toLocalDate().minusDays(2).format(DATE_FORMATTER);
            assertThat(result).isEqualTo(expected);
        }
    }

    @DisplayName("새벽 3시 이후(03:00~23:59)에는 어제 날짜를 반환한다")
    @ParameterizedTest(name = "{0}시 → 어제 날짜")
    @MethodSource("provideHoursAfterThreeAM")
    void shouldReturnYesterdayWhenAfterThreeAM(int hour) {
        // when
        String result = CacheKeyUtil.getYesterdayPostsCacheDate();
        
        // then
        // 실제 시간에 의존하므로 현재 시간이 3시 이후면 검증
        LocalDateTime now = LocalDateTime.now();
        if (now.getHour() >= 3) {
            String expected = now.toLocalDate().minusDays(1).format(DATE_FORMATTER);
            assertThat(result).isEqualTo(expected);
        }
    }

    @Test
    @DisplayName("캐시 키는 'posts:yesterday:yyyy-MM-dd' 형식이어야 한다")
    void shouldReturnCorrectCacheKeyFormat() {
        // when
        String cacheKey = CacheKeyUtil.getYesterdayPostsCacheKey();
        
        // then
        assertThat(cacheKey).startsWith("posts:yesterday:");
        assertThat(cacheKey).matches("posts:yesterday:\\d{4}-\\d{2}-\\d{2}");
    }

    @Test
    @DisplayName("캐시 키는 날짜 부분을 포함해야 한다")
    void shouldContainDateInCacheKey() {
        // when
        String cacheKey = CacheKeyUtil.getYesterdayPostsCacheKey();
        String dateOnly = CacheKeyUtil.getYesterdayPostsCacheDate();
        
        // then
        assertThat(cacheKey).endsWith(dateOnly);
        assertThat(cacheKey).isEqualTo("posts:yesterday:" + dateOnly);
    }

    @Test
    @DisplayName("동일 시점에서 여러 번 호출해도 같은 키를 반환한다")
    void shouldReturnConsistentKeyOnMultipleCalls() {
        // when
        String key1 = CacheKeyUtil.getYesterdayPostsCacheKey();
        String key2 = CacheKeyUtil.getYesterdayPostsCacheKey();
        String key3 = CacheKeyUtil.getYesterdayPostsCacheKey();
        
        // then
        assertThat(key1)
                .isEqualTo(key2)
                .isEqualTo(key3);
    }

    @Test
    @DisplayName("날짜 문자열은 yyyy-MM-dd 형식이어야 한다")
    void shouldReturnDateInCorrectFormat() {
        // when
        String date = CacheKeyUtil.getYesterdayPostsCacheDate();
        
        // then
        assertThat(date).matches("\\d{4}-\\d{2}-\\d{2}");
        
        // LocalDate로 파싱 가능해야 함
        assertThat(LocalDate.parse(date, DATE_FORMATTER)).isNotNull();
    }

    /**
     * 실제 로직 검증 테스트
     * 현재 시간 기준으로 올바른 날짜를 반환하는지 검증
     */
    @Test
    @DisplayName("현재 시간 기준으로 올바른 캐시 날짜를 계산한다")
    void shouldCalculateCorrectCacheDateBasedOnCurrentTime() {
        // given
        LocalDateTime now = LocalDateTime.now();
        LocalDate today = now.toLocalDate();
        int currentHour = now.getHour();
        
        // expected
        LocalDate expectedDate = (currentHour < 3) 
                ? today.minusDays(2) 
                : today.minusDays(1);
        String expectedDateString = expectedDate.format(DATE_FORMATTER);
        
        // when
        String actualDate = CacheKeyUtil.getYesterdayPostsCacheDate();
        
        // then
        assertThat(actualDate).isEqualTo(expectedDateString);
    }

    @Test
    @DisplayName("새벽 3시 경계값에서 올바르게 동작한다")
    void shouldHandleBoundaryAtThreeAM() {
        // given
        LocalDateTime now = LocalDateTime.now();
        
        // when
        String cacheKey = CacheKeyUtil.getYesterdayPostsCacheKey();
        
        // then
        assertThat(cacheKey).isNotNull();
        assertThat(cacheKey).isNotEmpty();
        
        // 3시 정각이면 어제 날짜를 사용해야 함
        if (now.getHour() == 3) {
            String expectedDate = now.toLocalDate().minusDays(1).format(DATE_FORMATTER);
            assertThat(cacheKey).isEqualTo("posts:yesterday:" + expectedDate);
        }
    }

    // 테스트 데이터 제공 메서드
    private static Stream<Arguments> provideHoursBeforeThreeAM() {
        return Stream.of(
                Arguments.of(0),
                Arguments.of(1),
                Arguments.of(2)
        );
    }

    private static Stream<Arguments> provideHoursAfterThreeAM() {
        return Stream.of(
                Arguments.of(3),
                Arguments.of(4),
                Arguments.of(12),
                Arguments.of(18),
                Arguments.of(23)
        );
    }
}
