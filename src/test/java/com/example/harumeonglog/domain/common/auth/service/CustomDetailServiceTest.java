package com.example.harumeonglog.domain.common.auth.service;

import com.example.harumeonglog.domain.member.exception.MemberException;
import com.example.harumeonglog.domain.mock.FakeMemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CustomDetailServiceTest {

    private CustomDetailService customDetailService;

    @BeforeEach
    void init() {
        this.customDetailService = new CustomDetailService(new FakeMemberRepository());
    }

    @Test
    @DisplayName("유저 이름으로 유저 찾기")
    void loadUserByUsername() {
        // given
        String success = "success@email.com";
        String username = "username@email.com";

        // when
        UserDetails userDetails = customDetailService.loadUserByUsername(success);
        Assertions.assertThrows(MemberException.class, () -> {
            customDetailService.loadUserByUsername(username);
        });

        // then
        assertThat(userDetails.getUsername()).isEqualTo(success);
    }
}
