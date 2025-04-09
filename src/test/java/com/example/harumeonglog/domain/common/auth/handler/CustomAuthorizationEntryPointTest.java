package com.example.harumeonglog.domain.common.auth.handler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;


class CustomAuthorizationEntryPointTest {

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    private CustomAuthorizationEntryPoint customAuthorizationEntryPoint;

    @BeforeEach
    void init() {
        this.customAuthorizationEntryPoint = new CustomAuthorizationEntryPoint();
        this.request = new MockHttpServletRequest();
        this.response = new MockHttpServletResponse();
    }

    @Test
    @DisplayName("commence")
    void commence() throws Exception {
        // given
        AuthenticationException exception = new BadCredentialsException("Test");

        // when
        customAuthorizationEntryPoint.commence(request, response, exception);

        // then
    }
}
