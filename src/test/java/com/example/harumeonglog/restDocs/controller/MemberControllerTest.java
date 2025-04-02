package com.example.harumeonglog.restDocs.controller;

import com.example.harumeonglog.domain.member.controller.MemberController;
import com.example.harumeonglog.domain.member.controller.dto.request.MemberRequest;
import com.example.harumeonglog.domain.member.controller.dto.request.SettingRequest;
import com.example.harumeonglog.domain.member.controller.dto.response.MemberResponse;
import com.example.harumeonglog.domain.member.controller.port.MemberService;
import com.example.harumeonglog.domain.member.controller.port.SettingService;
import com.example.harumeonglog.domain.member.domain.Member;
import com.example.harumeonglog.domain.member.domain.Setting;
import com.example.harumeonglog.domain.member.domain.enums.SocialType;
import com.example.harumeonglog.restDocs.base.AbstractRestDocsTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MemberController.class)
class MemberControllerTest extends AbstractRestDocsTest {

    @MockitoBean
    MemberService memberService;

    @MockitoBean
    SettingService settingService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("로그인")
    void login() throws Exception {
        // given
        MemberRequest.MemberLoginRequest dto = new MemberRequest.MemberLoginRequest(
                "test@naver.com",
                "test",
                LocalDate.now().minusYears(23),
                "KAKAO",
                "346257443",
                "https://www.naver.com",
                "WhieFDSeiWFpdivkEjmFdsiAlqk"
        );
        String request = objectMapper.writeValueAsString(dto);
        given(memberService.login(any(MemberRequest.MemberLoginRequest.class)))
                .willReturn(
                        MemberResponse.MemberLoginResponse.builder()
                                .memberId(1L)
                                .accessToken("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VybmFtZSIsImF1dGhvcml6YXRpb24iOlt7ImF1dGhvcml0eSI6IlJPTEVfVVNFUiJ9XSwiaWF0IjoxNzQy...")
                                .refreshToken("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VybmFtZSIsImF1dGhvcml6YXRpb24iOlt7ImF1dGhvcml0eSI6IlJPTEVfVVNFUiJ9XSwiaWF0IjoxNzQy...")
                                .build()
                );

        // when
        ResultActions result = mockMvc.perform(post("/members/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(request)
        );

        // then
        result
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestFields(
                                fieldWithPath("email").description("소셜 로그인 이메일"),
                                fieldWithPath("nickname").description("사용자 닉네임"),
                                fieldWithPath("birth").description("사용자 생일"),
                                fieldWithPath("socialType").description("소셜 로그인 종류 \n(KAKAO, APPLE)"),
                                fieldWithPath("providerId").description("소셜 로그인 사용자 Id"),
                                fieldWithPath("image").description("사용자 프로필 이미지"),
                                fieldWithPath("idToken").description("OIDC token")
                        ),
                        commonResponse,
                        responseFields(
                                beneathPath("result").withSubsectionId("result"),
                                fieldWithPath("memberId").description("사용자 ID"),
                                fieldWithPath("accessToken").description("Access Token"),
                                fieldWithPath("refreshToken").description("Refresh Token")
                        )
                ));
    }

    @Test
    @DisplayName("로그아웃")
    void logout() throws Exception {
        // given
        given(memberService.logout(any(Member.class)))
                .willReturn(
                        MemberResponse.MemberLogoutResponse.builder()
                                .memberId(1L)
                                .build()
                );

        // when
        ResultActions result = mockMvc.perform(post("/members/logout")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        result
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        commonResponse,
                        responseFields(
                                beneathPath("result").withSubsectionId("result"),
                                fieldWithPath("memberId").description("로그아웃된 사용자 Id")
                        )
                ));
    }

    @Test
    @DisplayName("사용자 정보 가져오기")
    void getInfo() throws Exception {
        // given
        MemberResponse.MemberInfoResponse response = MemberResponse.MemberInfoResponse.builder()
                .memberId(1L)
                .email("test@naver.com")
                .nickname("test")
                .image("https://www.naver.com")
                .build();

        // when
        ResultActions result = mockMvc.perform(get("/members/info"));

        // then
        result
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        commonResponse,
                        responseFields(
                                beneathPath("result").withSubsectionId("result"),
                                fieldWithPath("memberId").description("사용자 ID"),
                                fieldWithPath("email").description("사용자 이메일"),
                                fieldWithPath("nickname").description("사용자 닉네임"),
                                fieldWithPath("image").description("사용자 프로필 사진")

                        )
                ));
    }

    @Test
    @DisplayName("사용자 정보 변경")
    void updateInfo() throws Exception {
        // given
        LocalDate birth = LocalDate.now().minusYears(24);
        MemberRequest.MemberInfoUpdateRequest dto = new MemberRequest.MemberInfoUpdateRequest(
                "https://www.google.com",
                "google",
                birth
        );
        given(memberService.updateInfo(any(Member.class), any(MemberRequest.MemberInfoUpdateRequest.class)))
                .willReturn(Member.builder()
                        .id(1L)
                        .email("test@naver.com")
                        .nickname("google")
                        .birth(birth)
                        .socialType(SocialType.KAKAO)
                        .image("https://www.google.com")
                        .providerId("346257443")
                        .deletedAt(null)
                        .createdAt(LocalDateTime.now().minusDays(4))
                        .updatedAt(LocalDateTime.now())
                        .build()
                );
        String request = objectMapper.writeValueAsString(dto);

        // when
        ResultActions result = mockMvc.perform(patch("/members/info")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(request)
        );

        // then
        result
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestFields(
                                fieldWithPath("image").description("프로필 사진"),
                                fieldWithPath("nickname").description("사용자 이름"),
                                fieldWithPath("birth").description("사용자 생년월일")
                        ),
                        commonResponse,
                        responseFields(
                                beneathPath("result").withSubsectionId("result"),
                                fieldWithPath("memberId").description("사용자 Id"),
                                fieldWithPath("image").description("사용자 프로필 사진"),
                                fieldWithPath("nickname").description("사용자 닉네임"),
                                fieldWithPath("birth").description("사용자 생년월일")
                        )
                ));
    }

    @Test
    @DisplayName("환경 설정 변경")
    void updateSetting() throws Exception {
        // given
        SettingRequest.SettingUpdateRequest dto = new SettingRequest.SettingUpdateRequest(
                true,
                true,
                true,
                false
        );
        String request = objectMapper.writeValueAsString(dto);
        given(settingService.updateSetting(any(Member.class), any(SettingRequest.SettingUpdateRequest.class)))
                .willReturn(
                        Setting.builder()
                                .id(1L)
                                .morningAlarm(true)
                                .eventAlarm(true)
                                .articleLikeAlarm(true)
                                .commentAlarm(false)
                                .member(Member.builder().id(1L).build())
                                .createdAt(LocalDateTime.now().minusDays(3))
                                .updatedAt(LocalDateTime.now())
                                .build()
                );

        // when
        ResultActions result = mockMvc.perform(patch("/members/setting")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(request)
        );

        // then
        result
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestFields(
                                fieldWithPath("morningAlarm").description("아침 알림 설정"),
                                fieldWithPath("eventAlarm").description("일정 알림 설정"),
                                fieldWithPath("articleLikeAlarm").description("게시글 좋아요 알림 설정"),
                                fieldWithPath("commentAlarm").description("게시글 댓글 알림 설정")
                        ),
                        commonResponse,
                        responseFields(
                                beneathPath("result").withSubsectionId("result"),
                                fieldWithPath("memberId").description("사용자 ID"),
                                fieldWithPath("morningAlarm").description("아침 알림 설정 값"),
                                fieldWithPath("eventAlarm").description("일정 알림 설정 값"),
                                fieldWithPath("articleLikeAlarm").description("게시글 좋아요 알림 설정 값"),
                                fieldWithPath("commentAlarm").description("게시글 댓글 알림 설정 값")
                        )
                ));
    }
}
