package com.example.harumeonglog.domain.common.auth.util;

import com.example.harumeonglog.domain.common.auth.domain.CustomUserDetails;
import com.example.harumeonglog.domain.member.domain.Member;
import com.example.harumeonglog.domain.mock.FakeJwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class JwtUtilTest {

    private final String testSecret = "testtesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttest";
    private final long accessExpiration = 100000L;
    private final long refreshExpiration = 10000000L;
    private final String email = "email@email.com";

    private JwtUtil jwtUtil;

    @BeforeEach
    void init() {
        this.jwtUtil = new JwtUtil(testSecret, accessExpiration, refreshExpiration);
    }

    @Test
    @DisplayName("Access Token 생성")
    void createAccessToken() {
        // given
        CustomUserDetails customUserDetails = new CustomUserDetails(
                Member.builder()
                        .id(1L)
                        .email(this.email)
                        .providerId("providerId")
                        .image("http://www.naver.com")
                        .birth(LocalDate.now())
                        .nickname("nickname")
                        .build()
        );

        // when
        String accessToken = jwtUtil.createAccessToken(customUserDetails);

        // then
        assertThat(accessToken).isNotEmpty();
    }

    @Test
    @DisplayName("Refresh Token 생성")
    void createRefreshToken() {
        // given
        CustomUserDetails customUserDetails = new CustomUserDetails(
                Member.builder()
                        .id(1L)
                        .email(this.email)
                        .providerId("providerId")
                        .image("http://www.naver.com")
                        .birth(LocalDate.now())
                        .nickname("nickname")
                        .build()
        );

        // when
        String refreshToken = jwtUtil.createRefreshToken(customUserDetails);

        // then
        assertThat(refreshToken).isNotEmpty();
    }

    @Test
    @DisplayName("토큰 유효성 체크")
    void isValid() {
        // given
        CustomUserDetails customUserDetails = new CustomUserDetails(
                Member.builder()
                        .id(1L)
                        .email(this.email)
                        .providerId("providerId")
                        .image("http://www.naver.com")
                        .birth(LocalDate.now())
                        .nickname("nickname")
                        .build()
        );
        String token = FakeJwtUtil.createToken(customUserDetails, this.testSecret);

        // when
        boolean isValid = jwtUtil.isValid(token);
        boolean isNotValid = jwtUtil.isValid("invalid token");

        // then
        assertThat(isValid).isTrue();
        assertThat(isNotValid).isFalse();
    }

    @Test
    @DisplayName("사용자 정보 가져오기")
    void getUsername() {
        // given
        CustomUserDetails customUserDetails = new CustomUserDetails(
                Member.builder()
                        .id(1L)
                        .email(this.email)
                        .providerId("providerId")
                        .image("http://www.naver.com")
                        .birth(LocalDate.now())
                        .nickname("nickname")
                        .build()
        );
        String token = FakeJwtUtil.createToken(customUserDetails, this.testSecret);

        // when
        String username = jwtUtil.getUsername(token);
        String invalidUsername = jwtUtil.getUsername("invalid token");

        // then
        assertThat(username).isEqualTo(this.email);
        assertThat(invalidUsername).isNull();
    }

    @Test
    @DisplayName("Access Token 유효기간 가져오기")
    void getAccessExpiration() {
        // given

        // when
        Duration expiration = jwtUtil.getAccessExpiration();

        // then
        assertThat(expiration).hasMillis(accessExpiration);
    }

    @Test
    @DisplayName("Refresh Token 유효기간 가져오기")
    void getRefreshExpiration() {
        // given

        // when
        Duration expiration = jwtUtil.getRefreshExpiration();

        // then
        assertThat(expiration).hasMillis(refreshExpiration);
    }
}
