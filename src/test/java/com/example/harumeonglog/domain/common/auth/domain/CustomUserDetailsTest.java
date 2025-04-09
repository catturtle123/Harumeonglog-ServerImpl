package com.example.harumeonglog.domain.common.auth.domain;

import com.example.harumeonglog.domain.member.domain.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

class CustomUserDetailsTest {

    private CustomUserDetails customUserDetails;
    private Member member;

    @BeforeEach
    void init() {
        this.member = Member.builder()
                .id(1L)
                .email("email@email.com")
                .providerId("providerId")
                .image("http://www.naver.com")
                .birth(LocalDate.now())
                .nickname("nickname")
                .build();
        this.customUserDetails = new CustomUserDetails(this.member);
    }

    @Test
    @DisplayName("이름 가져오기")
    void getUsername() {
        // given

        // when
        String username = customUserDetails.getUsername();

        // then
        assertThat(username).isEqualTo("email@email.com");
    }

    @Test
    @DisplayName("비밀번호 가져오기")
    void getPassword() {
        // given

        // when
        String password = customUserDetails.getPassword();

        // then
        assertThat(password).isEqualTo("providerId");
    }

    @Test
    @DisplayName("권한 가져오기")
    void getAuthorities() {
        // given

        // when
        Collection<?> authorities = customUserDetails.getAuthorities();

        // then
        assertThat(authorities).isEqualTo(Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")));
    }

    @Test
    @DisplayName("사용 가능한지")
    void isEnabled() {
        // given

        // when
        boolean enable = customUserDetails.isEnabled();

        // then
        assertThat(enable).isTrue();
    }

    @Test
    @DisplayName("계정 만료 여부")
    void isAccountNonExpired() {
        // given

        // when
        boolean isAccountNonExpired = customUserDetails.isAccountNonExpired();

        // then
        assertThat(isAccountNonExpired).isTrue();
    }

    @Test
    @DisplayName("계정 잠금 여부")
    void isAccountNonLocked() {
        // given

        // when
        boolean isAccountNonLocked = customUserDetails.isAccountNonLocked();

        // then
        assertThat(isAccountNonLocked).isTrue();
    }

    @Test
    @DisplayName("비밀번호 만료 여부")
    void isCredentialsNonExpired() {
        // given

        // when
        boolean isCredentialsNonExpired = customUserDetails.isCredentialsNonExpired();

        // then
        assertThat(isCredentialsNonExpired).isTrue();
    }

    @Test
    @DisplayName("로그인 Member 가져오기")
    void getLoginMember() {
        // given

        // when
        Member loginMember = customUserDetails.getLoginMember();

        // then
        assertThat(loginMember).isEqualTo(loginMember);
    }
}

