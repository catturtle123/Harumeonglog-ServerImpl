package com.example.harumeonglog.restDocs.controller;

import com.example.harumeonglog.domain.pet.controller.PetImageController;
import com.example.harumeonglog.domain.pet.controller.dto.request.PetImageRequest;
import com.example.harumeonglog.domain.pet.controller.dto.response.PetImageResponse;
import com.example.harumeonglog.domain.pet.controller.port.PetImageService;
import com.example.harumeonglog.restDocs.base.AbstractRestDocsTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PetImageController.class)
public class PetImageControllerTest extends AbstractRestDocsTest {

    @MockitoBean
    private PetImageService petImageService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("이미지 추가 (다중)")
    void addImages() throws Exception {
        // given
        Long petId = 1L;
        PetImageRequest.AddImagesRequest request = new PetImageRequest.AddImagesRequest();
        request.setImageKeys(List.of("image1.jpg", "image2.jpg"));

        given(petImageService.addImages(eq(petId), any(PetImageRequest.AddImagesRequest.class)))
                .willReturn(PetImageResponse.AddImagesResponse.builder().imageIds(List.of(1L, 2L)).build());

        String requestBody = objectMapper.writeValueAsString(request);

        // when
        ResultActions result = mockMvc.perform(post("/pet-images/{petId}", petId)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody));

        // then
        result
                .andExpect(status().isCreated())
                .andDo(restDocs.document(
                        pathParameters(
                                parameterWithName("petId").description("이미지를 추가할 펫 ID")
                        ),
                        requestFields(
                                fieldWithPath("imageKeys").description("추가할 이미지 키 목록").type("String[]")
                        ),
                        commonResponse,
                        responseFields(
                                beneathPath("result").withSubsectionId("result"),
                                fieldWithPath("imageIds").description("추가된 이미지 ID 목록").type("Long[]")
                        )
                ));
    }

    @Test
    @DisplayName("펫 별 이미지 조회")
    void getImages() throws Exception {
        // given
        Long petId = 1L;
        PetImageResponse.GetImagesResponse response = PetImageResponse.GetImagesResponse.builder()
                .images(List.of(
                        PetImageResponse.GetImagesResponse.ImageInfo.builder()
                                .imageId(1L)
                                .imageKey("image1.jpg")
                                .createdAt(LocalDateTime.of(2025, 3, 25, 10, 0))
                                .build()
                ))
                .cursor(1L)
                .hasNext(true)
                .build();

        // Mockito 설정
        given(petImageService.getImages(eq(petId), isNull(), eq(10))) // cursor가 null일 경우 명시
                .willReturn(response);

        // when
        ResultActions result = mockMvc.perform(get("/pet-images/{petId}", petId)
                .param("cursor", "") // 빈 문자열은 null로 처리됨
                .param("size", "10"));

        // then
        result
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        pathParameters(
                                parameterWithName("petId").description("펫 ID")
                        ),
                        queryParameters(
                                parameterWithName("cursor").description("마지막 이미지 ID (null이면 처음부터 조회)").optional(),
                                parameterWithName("size").description("페이지 크기 (기본값 10)")
                        ),
                        commonResponse,
                        responseFields(
                                beneathPath("result").withSubsectionId("result"),
                                subsectionWithPath("images").description("이미지 목록").type("ImageInfo[]"),
                                fieldWithPath("cursor").description("").type("Long"),
                                fieldWithPath("hasNext").description("").type("boolean")
                        ),
                        responseFields(
                                beneathPath("result.images[]").withSubsectionId("ImageInfo"),
                                fieldWithPath("imageId").description("이미지 ID").type(JsonFieldType.NUMBER),
                                fieldWithPath("imageKey").description("이미지 키").type(JsonFieldType.STRING),
                                fieldWithPath("createdAt").description("생성 시간").type(JsonFieldType.STRING)
                        )
                ));
    }

    @Test
    @DisplayName("최근 이미지 조회")
    void recentImages() throws Exception {
        // given
        given(petImageService.recentImages())
                .willReturn(PetImageResponse.RecentImagesResponse.builder()
                        .images(List.of(
                                PetImageResponse.RecentImagesResponse.ImageInfo.builder()
                                        .imageId(1L)
                                        .imageKey("image1.jpg")
                                        .createdAt(LocalDateTime.of(2025, 3, 25, 10, 0))
                                        .build()
                        ))
                        .build());

        // when
        ResultActions result = mockMvc.perform(get("/pet-images/recent"));

        // then
        result
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        commonResponse,
                        responseFields(
                                beneathPath("result").withSubsectionId("result"),
                                subsectionWithPath("images").description("최근 이미지 목록 (최대 10장)").type("ImageInfo[]")
                        ),
                        responseFields(
                                beneathPath("result.images").withSubsectionId("ImageInfo"),
                                fieldWithPath("imageId").description("이미지 ID"),
                                fieldWithPath("imageKey").description("이미지 키"),
                                fieldWithPath("createdAt").description("이미지 생성 시간").type("LocalDateTime")
                        )
                ));
    }

    @Test
    @DisplayName("단일 이미지 조회")
    void getImage() throws Exception {
        // given
        Long imageId = 1L;
        given(petImageService.getImage(anyLong()))
                .willReturn(PetImageResponse.GetImageResponse.builder()
                        .imageId(1L)
                        .imageKey("image1.jpg")
                        .createdAt(LocalDateTime.of(2025, 3, 25, 10, 0))
                        .build());

        // when
        ResultActions result = mockMvc.perform(get("/pet-images/image/{imageId}", imageId));

        // then
        result
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        pathParameters(
                                parameterWithName("imageId").description("조회할 이미지 ID")
                        ),
                        commonResponse,
                        responseFields(
                                beneathPath("result").withSubsectionId("result"),
                                fieldWithPath("imageId").description("이미지 ID"),
                                fieldWithPath("imageKey").description("이미지 키"),
                                fieldWithPath("createdAt").description("이미지 생성 시간").type("LocalDateTime")
                        )
                ));
    }

    @Test
    @DisplayName("단일 이미지 삭제")
    void deleteImage() throws Exception {
        // given
        Long imageId = 1L;
        doNothing().when(petImageService).deleteImage(imageId);

        // when
        ResultActions result = mockMvc.perform(delete("/pet-images/image/{imageId}", imageId));

        // then
        result
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        pathParameters(
                                parameterWithName("imageId").description("삭제할 이미지 ID")
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
    @DisplayName("다중 이미지 삭제")
    void deleteImages() throws Exception {
        // given
        Long petId = 1L;
        PetImageRequest.DeleteImagesRequest request = new PetImageRequest.DeleteImagesRequest();
        request.setImageIds(List.of(1L, 2L));

        doNothing().when(petImageService).deleteImages(eq(petId), any(PetImageRequest.DeleteImagesRequest.class));

        String requestBody = objectMapper.writeValueAsString(request);

        // when
        ResultActions result = mockMvc.perform(delete("/pet-images/{petId}", petId)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody));

        // then
        result
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        pathParameters(
                                parameterWithName("petId").description("펫 ID")
                        ),
                        requestFields(
                                fieldWithPath("imageIds").description("삭제할 이미지 ID 목록").type(JsonFieldType.ARRAY)
                        ),
                        responseFields(
                                fieldWithPath("isSuccess").description("요청 성공 여부").type(JsonFieldType.BOOLEAN),
                                fieldWithPath("code").description("응답 코드").type(JsonFieldType.STRING),
                                fieldWithPath("message").description("응답 메시지").type(JsonFieldType.STRING),
                                fieldWithPath("result").description("삭제 완료 메시지").type(JsonFieldType.STRING)
                        )
                ));
    }
}