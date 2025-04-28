package com.example.harumeonglog.domain.walk.controller.specification;

import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.walk.dto.request.WalkRequest;
import com.example.harumeonglog.domain.walk.dto.response.WalkResponse;
import com.example.harumeonglog.global.common.response.CustomResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@ApiResponse(responseCode = "COMMON200", description = "성공입니다.")
public interface WalkControllerSpecification {

    @Operation(summary = "산책 시작 API by 서정모", description = "산책 시작할 때 사용하는 API")
    CustomResponse<WalkResponse.WalkStartResponse> startWalk(Member member, WalkRequest.WalkStartRequest walkCreateRequest);

    @Operation(summary = "산책 일시 정지 API by 서정모", description = "산책을 일시정지할 때 사용하는 API")
    @Parameter(name = "walkId", description = "산책 ID")
    CustomResponse<WalkResponse.WalkPauseResponse> pauseWalk(Long walkId);

    @Operation(summary = "산책 재개 API by 서정모", description = "산책 다시 시작할 때 사용하는 API")
    @Parameter(name = "walkId", description = "산책 ID")
    CustomResponse<WalkResponse.WalkResumeResponse> resumeWalk(Long walkId, WalkRequest.WalkResumeRequest request);

    @Operation(summary = "산책 종료 API by 서정모", description = "산책 종료할 때 사용하는 API")
    @Parameter(name = "walkId", description = "산책 ID")
    CustomResponse<WalkResponse.WalkEndResponse> endWalk(Long walkId);

    @Operation(summary = "산책 위치 추가 API by 서정모", description = "산책 좌표 추가할 때 사용하는 API")
    @Parameter(name = "trackId", description = "경로 ID")
    CustomResponse<WalkResponse.PositionCreateResponse> addPosition(@PathVariable Long trackId, @RequestBody WalkRequest.PositionRequest request);

    @Operation(summary = "산책 가능한 펫 API by 서정모", description = "산책 가능한 펫들 가져오는 API")
    CustomResponse<WalkResponse.WalkAvailablePetListResponse> getAvailablePetList(Member member);

    @Operation(summary = "산책 가능한 사용자 API by 서정모", description = "산책 가능한 사용자 가져오는 API")
    CustomResponse<WalkResponse.WalkAvailableMemberListResponse> getAvailableMemberList(WalkRequest.AvailableMemberRequest request);

    @Operation(summary = "산책 공유 API by 서정모", description = "산책 공유하는 API")
    @Parameter(name = "walkId", description = "산책 ID")
    CustomResponse<WalkResponse.WalkShareResponse> shareWalk(Long walkId);

    @Operation(summary = "산책 좋아요 API by 서정모", description = "산책 좋아요 하는 API")
    @Parameter(name = "walkId", description = "산책 ID")
    CustomResponse<WalkResponse.WalkLikeResponse> likeWalk(Member member, Long walkId);
}
