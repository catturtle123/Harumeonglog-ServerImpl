package com.example.harumeonglog.domain.common.auth.handler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

class JwtTokenLogoutHandlerTest {

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    private JwtTokenLogoutHandler jwtTokenLogoutHandler;

    @BeforeEach
    void init() {
        this.jwtTokenLogoutHandler = new JwtTokenLogoutHandler();
        this.request = new MockHttpServletRequest();
        this.response = new MockHttpServletResponse();
    }

    @Test
    @DisplayName("onLogoutSuccess")
    void onLogoutSuccess() throws Exception {
        // given

        // when
        jwtTokenLogoutHandler.onLogoutSuccess(request, response, null);

        // then
    }
}
