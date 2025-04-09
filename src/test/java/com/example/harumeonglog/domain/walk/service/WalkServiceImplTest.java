package com.example.harumeonglog.domain.walk.service;

import com.example.harumeonglog.domain.walk.controller.dto.request.WalkRequest;
import com.example.harumeonglog.domain.walk.controller.dto.response.WalkResponse;
import com.example.harumeonglog.domain.walk.controller.port.WalkService;
import com.example.harumeonglog.domain.walk.domain.Track;
import com.example.harumeonglog.domain.walk.domain.Walk;
import com.example.harumeonglog.domain.walk.domain.WalkLike;
import com.example.harumeonglog.domain.walk.domain.WalkPosition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class WalkServiceImplTest {

    private WalkService walkService;

    @BeforeEach
    void init() {
        walkService = new WalkServiceImpl();
    }

    @Test
    @DisplayName("산책 생성")
    void createWalk() {
        // given
        WalkRequest.WalkCreateRequest dto = new WalkRequest.WalkCreateRequest(
                List.of(1L, 2L),
                List.of(1L, 2L),
                "title",
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
        Track track = new Track();
        WalkPosition walkPosition = new WalkPosition();

        // when
        Walk walk = walkService.createWalk(dto);

        // then
        assertThat(walk).isNull();
    }

    @Test
    @DisplayName("산책 리스트 검색")
    void getWalkList() {
        // given
        String sort = "RECOMMENDED";
        Long cursor = 0L;
        int offset = 10;

        // when
        WalkResponse.WalkSearchListResponse response = walkService.getWalkList(sort, cursor, offset);

        // then
        assertThat(response).isNull();
    }

    @Test
    @DisplayName("산책 세부사항 검색")
    void getWalkDetails() {
        // given
        Long id = 1L;

        // when
        WalkResponse.WalkDetailResponse response = walkService.getWalkDetails(id);

        // then
        assertThat(response).isNull();
    }


    @Test
    @DisplayName("산책 공유")
    void shareWalk() {
        // given
        Long id = 1L;

        // when
        Walk walk = walkService.shareWalk(id);

        // then
        assertThat(walk).isNull();
    }


    @Test
    @DisplayName("산책 좋아요")
    void likeWalk() {
        // given
        Long id = 1L;
        WalkLike walkLike = new WalkLike();

        // when
        Walk walk = walkService.likeWalk(id);

        // then
        assertThat(walk).isNull();
    }
}
