package com.example.harumeonglog.domain.common.auth.handler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.access.AccessDeniedException;


class CustomAccessDeniedHandlerTest {

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    private CustomAccessDeniedHandler customAccessDeniedHandler;

    @BeforeEach
    void init() {
        this.customAccessDeniedHandler = new CustomAccessDeniedHandler();
        this.request = new MockHttpServletRequest();
        this.response = new MockHttpServletResponse();
    }

    @Test
    @DisplayName("handle")
    void handle() throws Exception {
        // given
        AccessDeniedException exception = new AccessDeniedException("Test");

        // when
        customAccessDeniedHandler.handle(request, response, exception);

        // then
    }
}
