package com.example.harumeonglog.domain.walk.controller;

import com.example.harumeonglog.domain.common.controller.response.CustomResponse;
import com.example.harumeonglog.domain.member.domain.Member;
import com.example.harumeonglog.domain.pet.domain.Pet;
import com.example.harumeonglog.domain.walk.controller.dto.request.MemberWalkRequest;
import com.example.harumeonglog.domain.walk.controller.dto.request.WalkRequest;
import com.example.harumeonglog.domain.walk.controller.dto.response.MemberWalkResponse;
import com.example.harumeonglog.domain.walk.controller.dto.response.WalkPetResponse;
import com.example.harumeonglog.domain.walk.controller.dto.response.WalkResponse;
import com.example.harumeonglog.domain.walk.controller.port.MemberWalkService;
import com.example.harumeonglog.domain.walk.controller.port.WalkService;
import com.example.harumeonglog.domain.walk.domain.Walk;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/walks")
@RequiredArgsConstructor
public class WalkController {

    private final WalkService walkService;
    private final MemberWalkService memberWalkService;

    @PostMapping
    public ResponseEntity<CustomResponse<WalkResponse.WalkCreateResponse>> createWalk(@RequestBody WalkRequest.WalkCreateRequest walkCreateRequest) {
        Walk walk = walkService.createWalk(walkCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(CustomResponse.created(WalkResponse.WalkCreateResponse.from(walk)));
    }

    @GetMapping
    public CustomResponse<WalkResponse.WalkSearchListResponse> getWalkList(@RequestParam(value = "sort", defaultValue = "RECOMMEND") String sort,
                                                                       @RequestParam(value = "cursor", required = false) Long cursor,
                                                                       @RequestParam(value = "size", defaultValue = "10") int offset) {
        WalkResponse.WalkSearchListResponse response = walkService.getWalkList(sort, cursor, offset);
        return CustomResponse.ok(response);
    }

    @GetMapping("/{walkId}")
    public CustomResponse<WalkResponse.WalkDetailResponse> getWalk(@PathVariable Long walkId) {
        WalkResponse.WalkDetailResponse response = walkService.getWalkDetails(walkId);
        return CustomResponse.ok(response);
    }

    @GetMapping("/pets")
    public CustomResponse<WalkPetResponse.WalkPetListResponse> getPetList() {
        // TODO: Annotation으로 변경 필요
        Member member = Member.builder().build();
        List<Pet> pets = memberWalkService.getPets(member);
        return CustomResponse.ok(WalkPetResponse.WalkPetListResponse.from(pets));
    }

    @PostMapping("/members")
    public CustomResponse<MemberWalkResponse.WalkMemberListResponse> getMemberList(@RequestBody MemberWalkRequest.PetMemberRequest request) {
        List<Member> members = memberWalkService.getMembers(request);
        return CustomResponse.ok(MemberWalkResponse.WalkMemberListResponse.from(members));
    }

    @PatchMapping("/{walkId}")
    public CustomResponse<WalkResponse.WalkShareResponse> shareWalk(@PathVariable Long walkId) {
        Walk walk = walkService.shareWalk(walkId);
        return CustomResponse.ok(WalkResponse.WalkShareResponse.from(walk));
    }

    @PostMapping("/{walkId}")
    public CustomResponse<WalkResponse.WalkLikeResponse> likeWalk(@PathVariable Long walkId) {
        Walk walk = walkService.likeWalk(walkId);
        return CustomResponse.ok(WalkResponse.WalkLikeResponse.from(walk));
    }
}
