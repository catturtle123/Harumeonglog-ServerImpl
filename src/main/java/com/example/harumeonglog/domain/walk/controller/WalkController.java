package com.example.harumeonglog.domain.walk.controller;

import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.walk.service.WalkCommandService;
import com.example.harumeonglog.domain.walk.service.WalkQueryService;
import com.example.harumeonglog.global.common.response.CustomResponse;
import com.example.harumeonglog.domain.walk.dto.request.WalkRequest;
import com.example.harumeonglog.domain.walk.dto.response.WalkResponse;
import com.example.harumeonglog.global.security.annotation.AuthenticatedMember;
import com.example.harumeonglog.global.validation.annotation.CheckCursorValidation;
import com.example.harumeonglog.global.validation.annotation.CheckSizeValidation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api/v1/walks")
@RequiredArgsConstructor
@Tag(name = "Walk", description = "산책 관련 API")
public class WalkController {

    private final WalkCommandService walkCommandService;
    private final WalkQueryService walkQueryService;

    @Operation(summary = "산책 시작 API by 서정모", description = "산책 시작할 때 사용하는 API")
    @ApiResponse(responseCode = "COMMON200", description = "성공입니다.")
    @PostMapping
    public CustomResponse<WalkResponse.WalkStartResponse> startWalk(@AuthenticatedMember Member member, @RequestBody WalkRequest.WalkStartRequest walkCreateRequest) {
        WalkResponse.WalkStartResponse response = walkCommandService.startWalk(member, walkCreateRequest);
        return CustomResponse.ok(response);
    }

    @Operation(summary = "산책 일시 정지 API by 서정모", description = "산책을 일시정지할 때 사용하는 API")
    @Parameter(name = "walkId", description = "산책 ID")
    @ApiResponse(responseCode = "COMMON200", description = "성공입니다.")
    @PatchMapping("/{walkId}/pause")
    public CustomResponse<WalkResponse.WalkPauseResponse> pauseWalk(@PathVariable Long walkId) {
        WalkResponse.WalkPauseResponse response = walkCommandService.pauseWalk(walkId);
        return CustomResponse.ok(response);
    }

    @Operation(summary = "산책 재개 API by 서정모", description = "산책 다시 시작할 때 사용하는 API")
    @Parameter(name = "walkId", description = "산책 ID")
    @ApiResponse(responseCode = "COMMON200", description = "성공입니다.")
    @PatchMapping("/{walkId}/resume")
    public CustomResponse<WalkResponse.WalkResumeResponse> resumeWalk(@PathVariable Long walkId, @RequestBody WalkRequest.WalkResumeRequest request) {
        WalkResponse.WalkResumeResponse response = walkCommandService.resumeWalk(walkId, request);
        return CustomResponse.ok(response);
    }

    @Operation(summary = "산책 종료 API by 서정모", description = "산책 종료할 때 사용하는 API")
    @Parameter(name = "walkId", description = "산책 ID")
    @ApiResponse(responseCode = "COMMON200", description = "성공입니다.")
    @PatchMapping("/{walkId}/end")
    public CustomResponse<WalkResponse.WalkEndResponse> endWalk(@PathVariable Long walkId, @RequestBody WalkRequest.WalkEndRequest request) {
        WalkResponse.WalkEndResponse response = walkCommandService.endWalk(walkId, request);
        return CustomResponse.ok(response);
    }

    @Operation(summary = "산책 위치 추가 API by 서정모", description = "산책 좌표 추가할 때 사용하는 API")
    @Parameter(name = "trackId", description = "경로 ID")
    @ApiResponse(responseCode = "COMMON200", description = "성공입니다.")
    @PostMapping("/tracks/{trackId}/positions")
    public CustomResponse<WalkResponse.PositionCreateResponse> addPosition(@PathVariable Long trackId, @RequestBody WalkRequest.PositionRequest request) {
        WalkResponse.PositionCreateResponse response = walkCommandService.addPosition(request, trackId);
        return CustomResponse.ok(response);
    }

    @Operation(summary = "산책 전체 조회 API by 서정모", description = "산책 전체 조회 API, SORT 부분 미완성")
    @ApiResponse(responseCode = "COMMON200", description = "성공입니다.")
    @GetMapping
    public CustomResponse<WalkResponse.WalkSearchListResponse> getWalkList(@AuthenticatedMember Member member,
                                                                           @RequestParam(value = "sort", defaultValue = "RECOMMEND") String sort,
                                                                           @RequestParam(value = "cursor", required = false) @CheckCursorValidation Long cursor,
                                                                           @RequestParam(value = "size", defaultValue = "10") @CheckSizeValidation Integer size) {
        WalkResponse.WalkSearchListResponse response = walkQueryService.getWalkList(member, sort, cursor, size);
        return CustomResponse.ok(response);
    }

    @Operation(summary = "산책 하나의 세부정보 조회 API by 서정모", description = "산책 세부 정보 조회")
    @Parameter(name = "walkId", description = "산책 ID")
    @ApiResponse(responseCode = "COMMON200", description = "성공입니다.")
    @GetMapping("/{walkId}")
    public CustomResponse<WalkResponse.WalkDetailResponse> getWalk(@AuthenticatedMember Member member, @PathVariable Long walkId) {
        WalkResponse.WalkDetailResponse response = walkQueryService.getWalkDetails(member, walkId);
        return CustomResponse.ok(response);
    }

    // TODO: 추후 커서 페이지네이션 필요
    @Operation(summary = "산책 가능한 펫 API by 서정모", description = "산책 가능한 펫들 가져오는 API")
    @ApiResponse(responseCode = "COMMON200", description = "성공입니다.")
    @GetMapping("/pets")
    public CustomResponse<WalkResponse.WalkAvailablePetListResponse> getAvailablePetList(@AuthenticatedMember Member member) {
        WalkResponse.WalkAvailablePetListResponse pets = walkQueryService.getAvailablePets(member);
        return CustomResponse.ok(pets);
    }

    // TODO: 추후 커서 페이지네이션 필요
    @Operation(summary = "산책 가능한 사용자 API by 서정모", description = "산책 가능한 사용자 가져오는 API")
    @ApiResponse(responseCode = "COMMON200", description = "성공입니다.")
    @PostMapping("/members")
    public CustomResponse<WalkResponse.WalkAvailableMemberListResponse> getAvailableMemberList(@RequestBody WalkRequest.AvailableMemberRequest request) {
        WalkResponse.WalkAvailableMemberListResponse members = walkQueryService.getAvailableMembers(request);
        return CustomResponse.ok(members);
    }

    @Operation(summary = "산책 정보 변경 API by 서정모", description = "산책 정보 변경하는 API")
    @Parameter(name = "walkId", description = "산책 ID")
    @ApiResponse(responseCode = "COMMON200", description = "성공입니다.")
    @PatchMapping("/{walkId}")
    public CustomResponse<WalkResponse.WalkUpdateResponse> updateWalk(@PathVariable Long walkId, @RequestBody WalkRequest.WalkUpdateRequest request) {
        WalkResponse.WalkUpdateResponse response = walkCommandService.updateWalk(walkId, request);
        return CustomResponse.ok(response);
    }

    @Operation(summary = "산책 공유 API by 서정모", description = "산책 공유하는 API")
    @Parameter(name = "walkId", description = "산책 ID")
    @ApiResponse(responseCode = "COMMON200", description = "성공입니다.")
    @PatchMapping("/{walkId}/share")
    public CustomResponse<WalkResponse.WalkShareResponse> shareWalk(@PathVariable Long walkId) {
        WalkResponse.WalkShareResponse response = walkCommandService.shareWalk(walkId);
        return CustomResponse.ok(response);
    }

    @Operation(summary = "산책 좋아요 API by 서정모", description = "산책 좋아요 하는 API")
    @Parameter(name = "walkId", description = "산책 ID")
    @ApiResponse(responseCode = "COMMON200", description = "성공입니다.")
    @PostMapping("/{walkId}")
    public CustomResponse<WalkResponse.WalkLikeResponse> likeWalk(@AuthenticatedMember Member member, @PathVariable Long walkId) {
        WalkResponse.WalkLikeResponse response = walkCommandService.likeWalk(member, walkId);
        return CustomResponse.ok(response);
    }
}
