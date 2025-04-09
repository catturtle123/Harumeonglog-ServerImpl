package com.example.harumeonglog.domain.member.service;

import com.example.harumeonglog.domain.member.controller.dto.request.MemberRequest;
import com.example.harumeonglog.domain.member.controller.dto.response.MemberResponse;
import com.example.harumeonglog.domain.member.controller.port.MemberService;
import com.example.harumeonglog.domain.member.domain.Invitation;
import com.example.harumeonglog.domain.member.domain.Member;
import com.example.harumeonglog.domain.member.domain.enums.SocialType;
import com.example.harumeonglog.domain.member.entity.MemberEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class MemberServiceImplTest {

    private MemberService memberService;

    @BeforeEach
    public void init() {
        memberService = MemberServiceImpl.builder().build();
    }

    @Test
    @DisplayName("로그인")
    void login() {
        // given
        MemberRequest.MemberLoginRequest request = MemberRequest.MemberLoginRequest.builder()
                .email("test@email.com")
                .birth(LocalDate.now())
                .nickname("test")
                .image("https://www.naver.com")
                .idToken("token")
                .providerId("id")
                .socialType("KAKAO")
                .build();

        // when
        MemberResponse.MemberLoginResponse response = memberService.login(request);

        // then
        assertThat(response).isNull();
    }

    @Test
    @DisplayName("로그아웃")
    void logout() {
        // given
        Member member = Member.builder()
                .email("test@email.com")
                .birth(LocalDate.now())
                .image("https://www.naver.com")
                .providerId("id")
                .socialType(SocialType.KAKAO)
                .createdAt(LocalDateTime.now().minusDays(1))
                .updatedAt(LocalDateTime.now())
                .deletedAt(null)
                .build();
        // when
        MemberResponse.MemberLogoutResponse response = memberService.logout(member);

        // then
        assertThat(response).isNull();
    }

    @Test
    @DisplayName("정보 수정")
    void updateInfo() {
        // given
        MemberEntity memberEntity = MemberEntity.builder()
                .email("test@email.com")
                .birth(LocalDate.now())
                .image("https://www.naver.com")
                .providerId("id")
                .socialType(SocialType.KAKAO)
                .deletedAt(null)
                .build();
        Member member = memberEntity.toModel();

        MemberRequest.MemberInfoUpdateRequest request = MemberRequest.MemberInfoUpdateRequest.builder()
                .birth(LocalDate.now())
                .image("https://www.naver.com")
                .nickname("test1")
                .build();

        // when
        Member updatedMember = memberService.updateInfo(member, request);

        // then
        assertThat(updatedMember).isNull();
    }

    @Test
    @DisplayName("초대")
    void invitation() {
        Invitation invitation = new Invitation();
    }
}
