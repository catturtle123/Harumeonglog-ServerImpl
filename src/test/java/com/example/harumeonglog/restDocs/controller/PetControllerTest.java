package com.example.harumeonglog.restDocs.controller;

import com.example.harumeonglog.domain.member.domain.enums.SocialType;
import com.example.harumeonglog.domain.pet.controller.PetController;
import com.example.harumeonglog.domain.pet.controller.dto.request.PetRequest;
import com.example.harumeonglog.domain.pet.controller.dto.response.PetResponse;
import com.example.harumeonglog.domain.pet.controller.port.PetService;
import com.example.harumeonglog.domain.pet.domain.enums.Gender;
import com.example.harumeonglog.domain.pet.domain.enums.MemberPetRole;
import com.example.harumeonglog.domain.pet.domain.enums.PetSize;
import com.example.harumeonglog.restDocs.base.AbstractRestDocsTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PetController.class)
public class PetControllerTest extends AbstractRestDocsTest {

    @MockitoBean
    private PetService petService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("펫 추가")
    void addPet() throws Exception {
        // given
        PetRequest.AddPetRequest request = new PetRequest.AddPetRequest();
        request.setName("Max");
        request.setSize(PetSize.MEDIUM);
        request.setType("Dog");
        request.setGender(Gender.MALE);
        request.setBirth(LocalDate.of(2020, 1, 1));
        request.setMainImage("max.jpg");

        given(petService.addPet(any(PetRequest.AddPetRequest.class)))
                .willReturn(PetResponse.AddPetResponse.builder().petId(1L).build());

        String requestBody = objectMapper.writeValueAsString(request);

        // when
        ResultActions result = mockMvc.perform(post("/pets")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody));

        // then
        result
                .andExpect(status().isCreated())
                .andDo(restDocs.document(
                        requestFields(
                                fieldWithPath("name").description("펫 이름"),
                                fieldWithPath("size").description("펫 크기 (SMALL, MEDIUM, BIG)"),
                                fieldWithPath("type").description("펫 종류"),
                                fieldWithPath("gender").description("펫 성별 (MALE, FEMALE)"),
                                fieldWithPath("birth").description("펫 생일").type("LocalDate"),
                                fieldWithPath("mainImage").description("펫 대표 이미지 URL")
                        ),
                        commonResponse,
                        responseFields(
                                beneathPath("result").withSubsectionId("result"),
                                fieldWithPath("petId").description("생성된 펫 ID")
                        )
                ));
    }

    @Test
    @DisplayName("펫 정보 변경")
    void changePetInfo() throws Exception {
        // given
        Long petId = 1L;
        PetRequest.ChangePetInfoRequest request = new PetRequest.ChangePetInfoRequest();
        request.setName("Max Updated");
        request.setSize(PetSize.BIG);
        request.setType("Dog");
        request.setGender(Gender.MALE);
        request.setBirth(LocalDate.of(2020, 1, 1));
        request.setMainImage("max_updated.jpg");

        given(petService.changePetInfo(eq(petId), any(PetRequest.ChangePetInfoRequest.class)))
                .willReturn(PetResponse.ChangePetInfoResponse.builder()
                        .petId(petId)
                        .name("Max Updated")
                        .size(PetSize.BIG)
                        .type("Dog")
                        .gender(Gender.MALE)
                        .birth(LocalDate.of(2020, 1, 1))
                        .mainImage("max_updated.jpg")
                        .build());

        String requestBody = objectMapper.writeValueAsString(request);

        // when
        ResultActions result = mockMvc.perform(put("/pets/{petId}", petId)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody));

        // then
        result
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        pathParameters(
                                parameterWithName("petId").description("변경할 펫 ID")
                        ),
                        requestFields(
                                fieldWithPath("name").description("펫 이름"),
                                fieldWithPath("size").description("펫 크기 (SMALL, MEDIUM, BIG)"),
                                fieldWithPath("type").description("펫 종류"),
                                fieldWithPath("gender").description("펫 성별 (MALE, FEMALE)"),
                                fieldWithPath("birth").description("펫 생일").type("LocalDate"),
                                fieldWithPath("mainImage").description("펫 대표 이미지 URL")
                        ),
                        commonResponse,
                        responseFields(
                                beneathPath("result").withSubsectionId("result"),
                                fieldWithPath("petId").description("펫 ID"),
                                fieldWithPath("name").description("펫 이름"),
                                fieldWithPath("size").description("펫 크기"),
                                fieldWithPath("type").description("펫 종류"),
                                fieldWithPath("gender").description("펫 성별"),
                                fieldWithPath("birth").description("펫 생일").type("LocalDate"),
                                fieldWithPath("mainImage").description("펫 대표 이미지 URL")
                        )
                ));
    }

    @Test
    @DisplayName("보유 펫 조회")
    void getPets() throws Exception {
        // given
        given(petService.getPets(null, 10))
                .willReturn(PetResponse.GetPetsResponse.builder()
                        .pets(List.of(
                                PetResponse.PetInfo.builder()
                                        .role("OWNER")
                                        .petId(1L)
                                        .name("Max")
                                        .size(PetSize.MEDIUM)
                                        .type("Dog")
                                        .gender(Gender.MALE)
                                        .birth(LocalDate.of(2020, 1, 1))
                                        .mainImage("max.jpg")
                                        .people(List.of(
                                                PetResponse.PeopleInfo.builder()
                                                        .id(1L)
                                                        .name("test")
                                                        .role("GUEST")
                                                        .build()
                                        ))
                                        .build()
                        ))
                        .cursor(1L)
                        .hasNext(true)
                        .build());

        // when
        ResultActions result = mockMvc.perform(get("/pets")
                .param("cursor", "")
                .param("size", "10"));

        // then
        result
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        queryParameters(
                                parameterWithName("cursor").description("마지막 펫 ID (null이면 처음부터 조회)").optional(),
                                parameterWithName("size").description("페이지 크기 (기본값 10)")
                        ),
                        commonResponse,
                        responseFields(
                                beneathPath("result").withSubsectionId("result"),
                                fieldWithPath("hasNext").description("다음 페이지 존재 여부"),
                                fieldWithPath("cursor").description("다음 페이지 커서"),
                                subsectionWithPath("pets").description("펫 목록").type("PetInfo[]")
                        ),
                        responseFields(
                                beneathPath("result.pets[]").withSubsectionId("PetInfo"),
                                fieldWithPath("role").description("권한"),
                                fieldWithPath("petId").description("펫 ID"),
                                fieldWithPath("name").description("펫 이름"),
                                fieldWithPath("size").description("펫 크기"),
                                fieldWithPath("type").description("펫 종류"),
                                fieldWithPath("gender").description("펫 성별"),
                                fieldWithPath("birth").description("펫 생일").type("LocalDate"),
                                fieldWithPath("mainImage").description("펫 대표 이미지 URL"),
                                subsectionWithPath("people").description("관련 사람 정보").type("PeopleInfo[]")
                        ),
                        responseFields(
                                beneathPath("result.pets[].people").withSubsectionId("PeopleInfo"),
                                fieldWithPath("[].id").description("사람 id"),
                                fieldWithPath("[].name").description("사람 이름"),
                                fieldWithPath("[].role").description("사람 권한")
                        )
                ));
    }


    @Test
    @DisplayName("현재 펫 변경")
    void changeCurrentPet() throws Exception {
        // given
        PetRequest.ChangeCurrentPetRequest request = new PetRequest.ChangeCurrentPetRequest();
        request.setPetId(1L);

        given(petService.changeCurrentPet(any(PetRequest.ChangeCurrentPetRequest.class)))
                .willReturn(PetResponse.ChangeCurrentPetResponse.builder()
                        .petId(1L)
                        .name("Max")
                        .build());

        String requestBody = objectMapper.writeValueAsString(request);

        // when
        ResultActions result = mockMvc.perform(patch("/pets/current")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody));

        // then
        result
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestFields(
                                fieldWithPath("petId").description("변경할 펫 ID")
                        ),
                        commonResponse,
                        responseFields(
                                beneathPath("result").withSubsectionId("result"),
                                fieldWithPath("petId").description("변경된 펫 ID"),
                                fieldWithPath("name").description("변경된 펫 이름")
                        )
                ));
    }

    @Test
    @DisplayName("펫 삭제")
    void deletePet() throws Exception {
        // given
        Long petId = 1L;
        doNothing().when(petService).deletePet(anyLong());

        // when
        ResultActions result = mockMvc.perform(patch("/pets/{petId}", petId));

        // then
        result
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        pathParameters(
                                parameterWithName("petId").description("삭제할 펫 ID")
                        ),
                        responseFields(
                                fieldWithPath("isSuccess").description("요청 성공 여부").type(JsonFieldType.BOOLEAN),
                                fieldWithPath("code").description("응답 코드").type(JsonFieldType.STRING),
                                fieldWithPath("message").description("응답 메시지").type(JsonFieldType.STRING),
                                fieldWithPath("result").description("삭제 완료 메시지").type(JsonFieldType.STRING)
                        )
                ));
    }

    @Test
    @DisplayName("펫에 초대")
    void invite() throws Exception {
        // given
        Long petId = 1L;
        PetRequest.InviteRequest request = new PetRequest.InviteRequest();
        request.setEmail("test@test.com");
        request.setSocialType(SocialType.KAKAO);

        doNothing().when(petService).invite(eq(petId), any(PetRequest.InviteRequest.class));

        String requestBody = objectMapper.writeValueAsString(request);

        // when
        ResultActions result = mockMvc.perform(post("/pets/{petId}/invite", petId)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody));

        // then
        result
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        pathParameters(
                                parameterWithName("petId").description("초대할 펫 ID")
                        ),
                        requestFields(
                                fieldWithPath("email").description("초대할 멤버의 이메일"),
                                fieldWithPath("socialType").description("소셜 로그인 유형 (KAKAO, GOOGLE 등)")
                        ),
                        responseFields(
                                fieldWithPath("isSuccess").description("요청 성공 여부").type(JsonFieldType.BOOLEAN),
                                fieldWithPath("code").description("응답 코드").type(JsonFieldType.STRING),
                                fieldWithPath("message").description("응답 메시지").type(JsonFieldType.STRING),
                                fieldWithPath("result").description("초대 완료 메시지").type(JsonFieldType.STRING)
                        )
                ));
    }
    @Test
    @DisplayName("펫에 초대할 멤버 검색")
    void searchMember() throws Exception {
        // given
        PetResponse.SearchMemberResponse response = PetResponse.SearchMemberResponse.builder()
                .members(List.of(
                        PetResponse.SearchMemberResponse.MemberInfo.builder()
                                .memberId(2L)
                                .name("John Doe")
                                .build()
                ))
                .cursor(1L)
                .hasNext(true)
                .build();

        given(petService.searchMember("test@test.com", null, 10))
                .willReturn(response);


        // when
        ResultActions result = mockMvc.perform(get("/pets/search-member")
                .param("cursor", "")
                .param("size", "10")
                .param("email", "test@test.com"));

        // then
        result
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        queryParameters(
                                parameterWithName("cursor").optional().description("커서 기반 페이징 (댓글 ID)"),
                                parameterWithName("size").optional().description("페이지 크기"),
                                parameterWithName("email").optional().description("검색 이메일")
                        ),
                        commonResponse,
                        responseFields(
                                beneathPath("result").withSubsectionId("result"),
                                subsectionWithPath("members").description("검색된 멤버 목록").type("MemberInfo[]"),
                                fieldWithPath("hasNext").description("다음 페이지 존재 여부"),
                                fieldWithPath("cursor").description("다음 페이지 커서")
                        ),
                        responseFields(
                                beneathPath("result.members[]").withSubsectionId("MemberInfo"),
                                fieldWithPath("memberId").description("멤버 ID"),
                                fieldWithPath("name").description("멤버 이름")
                        )
                ));
    }

}