package com.example.harumeonglog.domain.walk.controller;

import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.walk.controller.specification.WalkControllerSpecification;
import com.example.harumeonglog.domain.walk.service.WalkCommandService;
import com.example.harumeonglog.domain.walk.service.WalkQueryService;
import com.example.harumeonglog.global.common.response.CustomResponse;
import com.example.harumeonglog.domain.walk.dto.request.WalkRequest;
import com.example.harumeonglog.domain.walk.dto.response.WalkResponse;
import com.example.harumeonglog.global.security.annotation.AuthenticatedMember;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/walks")
@RequiredArgsConstructor
public class WalkController implements WalkControllerSpecification {

    private final WalkCommandService walkCommandService;
    private final WalkQueryService walkQueryService;

    @PostMapping
    public CustomResponse<WalkResponse.WalkStartResponse> startWalk(@AuthenticatedMember Member member, @RequestBody WalkRequest.WalkStartRequest walkCreateRequest) {
        WalkResponse.WalkStartResponse response = walkCommandService.startWalk(member, walkCreateRequest);
        return CustomResponse.ok(response);
    }

    @PatchMapping("/{walkId}/pause")
    public CustomResponse<WalkResponse.WalkPauseResponse> pauseWalk(@PathVariable Long walkId) {
        WalkResponse.WalkPauseResponse response = walkCommandService.pauseWalk(walkId);
        return CustomResponse.ok(response);
    }

    @PatchMapping("/{walkId}/resume")
    public CustomResponse<WalkResponse.WalkResumeResponse> resumeWalk(@PathVariable Long walkId, @RequestBody WalkRequest.WalkResumeRequest request) {
        WalkResponse.WalkResumeResponse response = walkCommandService.resumeWalk(walkId, request);
        return CustomResponse.ok(response);
    }

    @PatchMapping("/{walkId}/end")
    public CustomResponse<WalkResponse.WalkEndResponse> endWalk(@PathVariable Long walkId) {
        WalkResponse.WalkEndResponse response = walkCommandService.endWalk(walkId);
        return CustomResponse.ok(response);
    }

    @PostMapping("/tracks/{trackId}/positions")
    public CustomResponse<WalkResponse.PositionCreateResponse> addPosition(@PathVariable Long trackId, @RequestBody WalkRequest.PositionRequest request) {
        WalkResponse.PositionCreateResponse response = walkCommandService.addPosition(request, trackId);
        return CustomResponse.ok(response);
    }

    @GetMapping
    public CustomResponse<WalkResponse.WalkSearchListResponse> getWalkList(@RequestParam(value = "sort", defaultValue = "RECOMMEND") String sort,
                                                                       @RequestParam(value = "cursor", required = false) Long cursor,
                                                                       @RequestParam(value = "size", defaultValue = "10") int offset) {
        WalkResponse.WalkSearchListResponse response = walkQueryService.getWalkList(sort, cursor, offset);
        return CustomResponse.ok(response);
    }

    @GetMapping("/{walkId}")
    public CustomResponse<WalkResponse.WalkDetailResponse> getWalk(@PathVariable Long walkId) {
        WalkResponse.WalkDetailResponse response = walkQueryService.getWalkDetails(walkId);
        return CustomResponse.ok(response);
    }

    // TODO: 추후 커서 페이지네이션 필요
    @GetMapping("/pets")
    public CustomResponse<WalkResponse.WalkAvailablePetListResponse> getAvailablePetList(@AuthenticatedMember Member member) {
        WalkResponse.WalkAvailablePetListResponse pets = walkQueryService.getAvailablePets(member);
        return CustomResponse.ok(pets);
    }

    // TODO: 추후 커서 페이지네이션 필요
    @PostMapping("/members")
    public CustomResponse<WalkResponse.WalkAvailableMemberListResponse> getAvailableMemberList(@RequestBody WalkRequest.AvailableMemberRequest request) {
        WalkResponse.WalkAvailableMemberListResponse members = walkQueryService.getAvailableMembers(request);
        return CustomResponse.ok(members);
    }

    @PatchMapping("/{walkId}")
    public CustomResponse<WalkResponse.WalkShareResponse> shareWalk(@PathVariable Long walkId) {
        WalkResponse.WalkShareResponse response = walkCommandService.shareWalk(walkId);
        return CustomResponse.ok(response);
    }

    @PostMapping("/{walkId}")
    public CustomResponse<WalkResponse.WalkLikeResponse> likeWalk(@AuthenticatedMember Member member, @PathVariable Long walkId) {
        WalkResponse.WalkLikeResponse response = walkCommandService.likeWalk(member, walkId);
        return CustomResponse.ok(response);
    }
}
