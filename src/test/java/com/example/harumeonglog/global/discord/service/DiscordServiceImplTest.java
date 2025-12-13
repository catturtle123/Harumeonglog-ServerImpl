package com.example.harumeonglog.global.discord.service;

import com.example.harumeonglog.global.discord.FakeDiscordApiUtilImpl;
import com.example.harumeonglog.global.discord.dto.DiscordMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("DiscordServiceImpl 테스트")
class DiscordServiceImplTest {

    private DiscordServiceImpl discordService;
    private FakeDiscordApiUtilImpl discordApiUtil;

    @BeforeEach
    void setUp() {
        discordApiUtil = new FakeDiscordApiUtilImpl();
        discordService = new DiscordServiceImpl(discordApiUtil);
    }

    @Test
    @DisplayName("에러 메시지를 Discord로 전송해야 한다")
    void shouldSendErrorMessageToDiscord() {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setMethod("GET");
        request.setRequestURI("/api/posts");
        request.setQueryString("page=1&size=10");
        
        Exception exception = new RuntimeException("테스트 예외");
        String errorMessage = "API 호출 중 오류 발생";

        // when
        discordService.sendErrorMessage(request, exception, errorMessage);

        // then
        assertThat(discordApiUtil.getSentMessageCount()).isEqualTo(1);
        
        DiscordMessage sentMessage = discordApiUtil.getLastSentMessage();
        assertThat(sentMessage).isNotNull();
        assertThat(sentMessage.getContent()).contains("에러 발생");
    }

    @Test
    @DisplayName("여러 메시지를 결합하여 전송해야 한다")
    void shouldCombineMultipleMessages() {
        // given
        MockHttpServletRequest request = createMockRequest();
        Exception exception = new NullPointerException("Null 참조 오류");
        String message1 = "첫 번째 메시지";
        String message2 = "두 번째 메시지";
        String message3 = "세 번째 메시지";

        // when
        discordService.sendErrorMessage(request, exception, message1, message2, message3);

        // then
        assertThat(discordApiUtil.getSentMessageCount()).isEqualTo(1);
        DiscordMessage sentMessage = discordApiUtil.getLastSentMessage();
        
        assertThat(sentMessage).isNotNull();
        assertThat(sentMessage.getEmbeds()).hasSize(2); // 에러 정보 + Stack Trace
    }

    @Test
    @DisplayName("HTTP 메서드와 URL 정보가 포함되어야 한다")
    void shouldIncludeHttpMethodAndUrl() {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setMethod("POST");
        request.setRequestURI("/api/posts/123");
        
        Exception exception = new IllegalArgumentException("잘못된 파라미터");

        // when
        discordService.sendErrorMessage(request, exception, "파라미터 검증 실패");

        // then
        DiscordMessage sentMessage = discordApiUtil.getLastSentMessage();
        assertThat(sentMessage).isNotNull();
        assertThat(sentMessage.getEmbeds().get(0).getDescription())
                .contains("POST")
                .contains("/api/posts/123");
    }

    @Test
    @DisplayName("쿼리 스트링이 있으면 URL에 포함되어야 한다")
    void shouldIncludeQueryStringInUrl() {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setMethod("GET");
        request.setRequestURI("/api/search");
        request.setQueryString("keyword=test&page=2");
        
        Exception exception = new RuntimeException("검색 실패");

        // when
        discordService.sendErrorMessage(request, exception, "검색 중 오류");

        // then
        DiscordMessage sentMessage = discordApiUtil.getLastSentMessage();
        assertThat(sentMessage.getEmbeds().get(0).getDescription())
                .contains("keyword=test&page=2");
    }

    @Test
    @DisplayName("Stack Trace가 포함되어야 한다")
    void shouldIncludeStackTrace() {
        // given
        MockHttpServletRequest request = createMockRequest();
        Exception exception = new RuntimeException("스택 트레이스 테스트");

        // when
        discordService.sendErrorMessage(request, exception, "테스트 메시지");

        // then
        DiscordMessage sentMessage = discordApiUtil.getLastSentMessage();
        assertThat(sentMessage.getEmbeds()).hasSize(2);
        assertThat(sentMessage.getEmbeds().get(1).getTitle()).contains("Stack Trace");
        assertThat(sentMessage.getEmbeds().get(1).getDescription())
                .contains("RuntimeException")
                .contains("스택 트레이스 테스트");
    }

    @Test
    @DisplayName("빈 메시지 배열도 처리할 수 있어야 한다")
    void shouldHandleEmptyMessageArray() {
        // given
        MockHttpServletRequest request = createMockRequest();
        Exception exception = new Exception("빈 메시지 테스트");

        // when
        discordService.sendErrorMessage(request, exception);

        // then
        assertThat(discordApiUtil.getSentMessageCount()).isEqualTo(1);
        DiscordMessage sentMessage = discordApiUtil.getLastSentMessage();
        assertThat(sentMessage).isNotNull();
    }

    @Test
    @DisplayName("여러 번 호출해도 모든 메시지가 전송되어야 한다")
    void shouldSendMultipleMessages() {
        // given
        MockHttpServletRequest request = createMockRequest();

        // when
        discordService.sendErrorMessage(request, new RuntimeException("첫 번째"), "메시지 1");
        discordService.sendErrorMessage(request, new RuntimeException("두 번째"), "메시지 2");
        discordService.sendErrorMessage(request, new RuntimeException("세 번째"), "메시지 3");

        // then
        assertThat(discordApiUtil.getSentMessageCount()).isEqualTo(3);
        assertThat(discordApiUtil.getSentMessages()).hasSize(3);
    }

    private MockHttpServletRequest createMockRequest() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setMethod("GET");
        request.setRequestURI("/api/test");
        return request;
    }
}
