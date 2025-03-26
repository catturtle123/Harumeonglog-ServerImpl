package com.example.harumeonglog.restDocs.controller;

import com.example.harumeonglog.domain.member.domain.Member;
import com.example.harumeonglog.domain.member.domain.enums.SocialType;
import com.example.harumeonglog.domain.pet.domain.Pet;
import com.example.harumeonglog.domain.pet.domain.enums.Gender;
import com.example.harumeonglog.domain.pet.domain.enums.PetSize;
import com.example.harumeonglog.domain.walk.controller.WalkController;
import com.example.harumeonglog.domain.walk.controller.dto.request.MemberWalkRequest;
import com.example.harumeonglog.domain.walk.controller.dto.request.WalkRequest;
import com.example.harumeonglog.domain.walk.controller.dto.response.WalkResponse;
import com.example.harumeonglog.domain.walk.controller.port.MemberWalkService;
import com.example.harumeonglog.domain.walk.controller.port.WalkService;
import com.example.harumeonglog.domain.walk.domain.Walk;
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
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.timeout;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WalkController.class)
public class WalkControllerTest extends AbstractRestDocsTest {

    @MockitoBean
    WalkService walkService;

    @MockitoBean
    MemberWalkService memberWalkService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("산책 생성")
    void createWalk() throws Exception {
        //given
        final String title = "공원 한바퀴 산책";
        WalkRequest.WalkCreateRequest dto = new WalkRequest.WalkCreateRequest(
                List.of(1L, 2L),
                List.of(1L, 2L),
                title,
                37.4869091281693,
                127.032363992706,
                2.2,
                23,
                List.of(
                        new WalkRequest.Track(List.of(
                        new WalkRequest.Position(37.4869091381693, 127.032364092706),
                        new WalkRequest.Position(37.4869091481693, 127.032364192706),
                        new WalkRequest.Position(37.4869091581693, 127.032364292706)
                        )),
                        new WalkRequest.Track(List.of(
                        new WalkRequest.Position(37.4869091681693, 127.032364392706),
                        new WalkRequest.Position(37.4869091781693, 127.032364492706)
                        ))
                )
        );
        given(walkService.createWalk(any(WalkRequest.WalkCreateRequest.class)))
                .willReturn(
                        Walk.builder()
                                .id(1L)
                                .title(title)
                                .distance(2.2)
                                .time(23)
                                .startLatitude(37.4869091281693)
                                .startLongitude(127.032363992706)
                                .walkLikeNum(0L)
                                .isShared(false)
                                .deletedAt(null)
                                .build()
                );
        String request = objectMapper.writeValueAsString(dto);

        //when
        ResultActions result = mockMvc.perform(post("/walks")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(request)
        );

        //then
        result
                .andExpect(status().isCreated())
                .andDo(restDocs.document(
                        requestFields(
                                fieldWithPath("petId").description("산책하는 강아지들의 ID List").type("Number[]"),
                                fieldWithPath("memberId").description("산책하는 사람들의 ID List").type("Number[]"),
                                fieldWithPath("title").description("산책의 제목"),
                                fieldWithPath("startLatitude").description("산책 시작점의 위도").type("Double"),
                                fieldWithPath("startLongitude").description("산책 시작점의 경도").type("Double"),
                                fieldWithPath("distance").description("산책 경로의 거리 (단위: Km)").type("Double"),
                                fieldWithPath("time").description("산책에 걸린 시간"),
                                subsectionWithPath("tracks").description("산책 경로들").type("Track[]").optional()
                        ),
                        requestFields(
                                beneathPath("tracks").withSubsectionId("Track"),
                                subsectionWithPath("positions").description("산책 경로들의 좌표들").type("Position[]").optional()
                        ),
                        requestFields(
                                beneathPath("tracks[].positions").withSubsectionId("Position"),
                                fieldWithPath("[].latitude").description("좌표의 위도").type("Double"),
                                fieldWithPath("[].longitude").description("좌표의 경도").type("Double")
                        ),
                        commonResponse,
                        responseFields(
                                beneathPath("result").withSubsectionId("result"),
                                fieldWithPath("walkId").description("생성된 산책 ID")
                        )
                ));
    }

    @Test
    @DisplayName("산책 경로 검색")
    void getWalkList() throws Exception {
        //given
        final String sort = "RECOMMEND";
        final String cursor = "1";
        final String size = "2";
        given(walkService.getWalkList(anyString(), anyLong(), anyInt()))
                .willReturn(WalkResponse.WalkSearchListResponse.builder()
                        .items(
                                List.of(
                                        WalkResponse.WalkSearchResponse.builder()
                                                .id(2L)
                                                .title("2 번째")
                                                .walkLikeNum(2L)
                                                .distance(1.4)
                                                .time(20)
                                                .memberNickname("뚜비")
                                                .isLike(false)
                                                .build(),
                                        WalkResponse.WalkSearchResponse.builder()
                                                .id(3L)
                                                .title("3 번째")
                                                .walkLikeNum(22L)
                                                .distance(1.7)
                                                .time(23)
                                                .memberNickname("나나")
                                                .isLike(true)
                                                .build()
                                )
                        )
                        .hasNext(true)
                        .cursor(4L)
                        .build()
                );

        //when
        ResultActions result = mockMvc.perform(get("/walks")
                .param("sort", sort)
                .param("cursor", cursor)
                .param("size", size)
        );

        //then
        result
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        queryParameters(
                                parameterWithName("sort").description("정렬 기준\n(RECOMMEND, DISTANCE, TIME)").attributes(defaultValue("RECOMMEND"), required(false)),
                                parameterWithName("cursor").description("커서 페이지네이션 적용 시 사용할 커서, 처음 검색은 불필요").attributes(required(false)),
                                parameterWithName("size").description("검색할 데이터 개수").attributes(defaultValue("10"), required(false))
                        ),
                        commonResponse,
                        responseFields(
                                beneathPath("result").withSubsectionId("result"),
                                subsectionWithPath("items").description("데이터 리스트").type("WalkSearchResponse[]"),
                                fieldWithPath("hasNext").description("다음 커서 페이지네이션 시 데이터가 있는지 여부"),
                                fieldWithPath("cursor").description("다음 커서 페이지네이션 시 넣을 커서")
                        ),
                        responseFields(
                                beneathPath("result.items").withSubsectionId("WalkSearchResponse"),
                                fieldWithPath("id").description("산책 고유 id"),
                                fieldWithPath("title").description("산책의 제목"),
                                fieldWithPath("walkLikeNum").description("산책의 좋아요 개수"),
                                fieldWithPath("distance").description("산책의 거리 (Km)").type("Double"),
                                fieldWithPath("time").description("산책의 소요 시간 (분)"),
                                fieldWithPath("memberNickname").description("산책을 공유한 사람의 닉네임"),
                                fieldWithPath("isLike").description("사용자가 이 산책에 좋아요를 눌렀는지의 여부")
                        )
                ));
    }

    @Test
    @DisplayName("산책 하나 검색")
    void getWalkDetails() throws Exception {
        //given
        final Long walkId = 1L;
        given(walkService.getWalkDetails(anyLong()))
                .willReturn(WalkResponse.WalkDetailResponse.builder()
                        .id(1L)
                        .title("산책 제목")
                        .walkLikeNum(12L)
                        .distance(2.2)
                        .time(32)
                        .memberNickname("뚜비")
                        .isLike(true)
                        .tracks(
                                List.of(
                                        WalkResponse.Track.builder()
                                                .trackId(1L)
                                                .positions(List.of(
                                                        WalkResponse.Position.builder()
                                                                .latitude(34.1234567)
                                                                .longitude(123.456789)
                                                                .build(),
                                                        WalkResponse.Position.builder()
                                                                .latitude(34.1235567)
                                                                .longitude(123.456889)
                                                                .build()
                                                ))
                                                .build()
                                )
                        )
                        .build()
                );

        //when
        ResultActions result = mockMvc.perform(get("/walks/{walkId}", walkId));

        //then
        result
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        pathParameters(
                                parameterWithName("walkId").description("검색할 산책 ID")
                        ),
                        commonResponse,
                        responseFields(
                                beneathPath("result").withSubsectionId("result"),
                                fieldWithPath("id").description("산책 고유 id"),
                                fieldWithPath("title").description("산책의 제목"),
                                fieldWithPath("walkLikeNum").description("산책의 좋아요 개수"),
                                fieldWithPath("distance").description("산책의 거리 (Km)").type("Double"),
                                fieldWithPath("time").description("산책의 소요 시간 (분)"),
                                fieldWithPath("memberNickname").description("산책을 공유한 사람의 닉네임"),
                                fieldWithPath("isLike").description("사용자가 이 산책에 좋아요를 눌렀는지의 여부"),
                                subsectionWithPath("tracks").description("산책의 경로들").type("Track[]")
                        ),
                        responseFields(
                                beneathPath("result.tracks").withSubsectionId("Track"),
                                fieldWithPath("trackId").description("경로의 ID"),
                                subsectionWithPath("positions").description("경로의 좌표들").type("Position[]")
                        ),
                        responseFields(
                                beneathPath("result.tracks[].positions").withSubsectionId("Position"),
                                fieldWithPath("[].latitude").description("좌표의 위도").type("Double"),
                                fieldWithPath("[].longitude").description("좌표의 경도").type("Double")
                        )
                ));
    }

    @Test
    @DisplayName("산책 공유 설정")
    void shareWalk() throws Exception {
        //given
        final Long walkId = 1L;
        given(walkService.shareWalk(anyLong()))
                .willReturn(
                        Walk.builder()
                                .id(1L)
                                .title("공원 한바퀴")
                                .distance(2.2)
                                .time(23)
                                .startLatitude(37.4869091281693)
                                .startLongitude(127.032363992706)
                                .walkLikeNum(23L)
                                .isShared(true)
                                .createdAt(LocalDateTime.now().minusDays(1L))
                                .updatedAt(LocalDateTime.now())
                                .build()
                );

        //when
        ResultActions result = mockMvc.perform(patch("/walks/{walkId}", walkId).contentType(MediaType.APPLICATION_JSON_VALUE));

        //then
        result
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        pathParameters(
                                parameterWithName("walkId").description("공유할 산책 ID")
                        ),
                        commonResponse,
                        responseFields(
                                beneathPath("result").withSubsectionId("result"),
                                fieldWithPath("walkId").description("산책 ID"),
                                fieldWithPath("distance").description("산책 거리"),
                                fieldWithPath("time").description("산책 시간"),
                                fieldWithPath("isShared").description("산책 공유 여부, API 실행 후 True값으로 반환")
                        )
                ));
    }

    @Test
    @DisplayName("산책 강아지 목록 불러오기")
    void getPets() throws Exception {
        //given
        given(memberWalkService.getPets(any(Member.class)))
                .willReturn(
                        List.of(
                                Pet.builder()
                                        .id(1L)
                                        .name("세로")
                                        .size(PetSize.MEDIUM)
                                        .type("이게 뭐지?")
                                        .gender(Gender.FEMALE)
                                        .birth(LocalDate.of(2002, 2, 2))
                                        .mainImage("https://www.naver.com")
                                        .createdAt(LocalDateTime.now())
                                        .updatedAt(LocalDateTime.now())
                                        .build(),
                                Pet.builder()
                                        .id(2L)
                                        .name("테라")
                                        .size(PetSize.BIG)
                                        .type("이게 뭐지?")
                                        .gender(Gender.MALE)
                                        .birth(LocalDate.of(2002, 2, 2))
                                        .mainImage("https://www.kakao.com")
                                        .createdAt(LocalDateTime.now())
                                        .updatedAt(LocalDateTime.now())
                                        .build()

                        )
                );

        //then
        ResultActions result = mockMvc.perform(get("/walks/pets"));

        //when
        result
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        commonResponse,
                        responseFields(
                                beneathPath("result").withSubsectionId("result"),
                                subsectionWithPath("pets").description("산책 가능한 반려견들").type("WalkPetInfoResponse[]"),
                                fieldWithPath("size").description("산책 가능한 반려견의 수")
                        ),
                        responseFields(
                                beneathPath("result.pets").withSubsectionId("WalkPetInfoResponse"),
                                fieldWithPath("petId").description("반려견 ID"),
                                fieldWithPath("name").description("반려견 이름"),
                                fieldWithPath("image").description("반려견 사진")
                        )
                ));
    }

    @Test
    @DisplayName("같이 산책할 사람 목록 불러오기")
    void getMembers() throws Exception {
        //given
        MemberWalkRequest.PetMemberRequest dto = new MemberWalkRequest.PetMemberRequest(List.of(
                1L, 2L
        ));
        String request = objectMapper.writeValueAsString(dto);
        given(memberWalkService.getMembers(any(MemberWalkRequest.PetMemberRequest.class)))
                .willReturn(List.of(
                        Member.builder()
                                .id(2L)
                                .email("email@email.com")
                                .nickname("뚜비")
                                .birth(LocalDate.of(2002, 2, 2))
                                .socialType(SocialType.KAKAO)
                                .image("https://www.naver.com")
                                .provider_id("342543342")
                                .createdAt(LocalDateTime.now())
                                .updatedAt(LocalDateTime.now())
                                .build(),
                        Member.builder()
                                .id(4L)
                                .email("email@naver.com")
                                .nickname("티비")
                                .birth(LocalDate.of(2002, 2, 2))
                                .socialType(SocialType.KAKAO)
                                .image("https://www.kakao.com")
                                .provider_id("34276842")
                                .createdAt(LocalDateTime.now())
                                .updatedAt(LocalDateTime.now())
                                .build()
                ));

        //when
        ResultActions result = mockMvc.perform(post("/walks/members")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(request)
        );

        //then
        result
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestFields(
                                fieldWithPath("petId").description("사용자가 선택한 반려견 ID 배열").type("Number[]")
                        ),
                        commonResponse,
                        responseFields(
                                beneathPath("result").withSubsectionId("result"),
                                subsectionWithPath("members").description("사용자 목록").type("WalkMemberResponse[]"),
                                fieldWithPath("size").description("선택 가능한 사용자 수")
                        ),
                        responseFields(
                                beneathPath("result.members").withSubsectionId("WalkMemberResponse"),
                                fieldWithPath("memberId").description("사용자 ID"),
                                fieldWithPath("nickname").description("사용자 닉네임"),
                                fieldWithPath("image").description("사용자 이미지")
                        )
                ));

    }

    @Test
    @DisplayName("산책 좋아요")
    void likeWalk() throws Exception {
        // given
        given(walkService.likeWalk(anyLong()))
                .willReturn(
                        Walk.builder()
                                .id(1L)
                                .title("산책")
                                .distance(2.2)
                                .time(23)
                                .startLatitude(34.1262342)
                                .startLongitude(120.1234123)
                                .walkLikeNum(23L)
                                .isShared(true)
                                .deletedAt(null)
                                .createdAt(LocalDateTime.now())
                                .updatedAt(LocalDateTime.now())
                                .build()
                );

        // when
        ResultActions result = mockMvc.perform(post("/walks/{walkId}", 1L)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        result
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        pathParameters(
                                parameterWithName("walkId").description("좋아요 혹은 좋아요 취소할 산책 ID")
                        ),
                        commonResponse,
                        responseFields(
                                beneathPath("result").withSubsectionId("result"),
                                fieldWithPath("walkId").description("산책 ID"),
                                fieldWithPath("walkLikeNum").description("산책 좋아요 수")
                        )
                ));


    }
}
