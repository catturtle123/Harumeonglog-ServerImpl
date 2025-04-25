package com.example.harumeonglog.domain.pet.controller;


import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.pet.controller.specification.PetControllerSpecification;
import com.example.harumeonglog.domain.pet.dto.request.PetRequest;
import com.example.harumeonglog.domain.pet.dto.request.PetRequest.AddPetRequest;
import com.example.harumeonglog.domain.pet.dto.request.PetRequest.ChangePetInfoRequest;
import com.example.harumeonglog.domain.pet.dto.response.PetResponse;
import com.example.harumeonglog.domain.pet.dto.response.PetResponse.AddPetResponse;
import com.example.harumeonglog.domain.pet.dto.response.PetResponse.ChangePetInfoResponse;
import com.example.harumeonglog.domain.pet.service.command.PetCommandService;
import com.example.harumeonglog.domain.pet.service.query.PetQueryService;
import com.example.harumeonglog.global.common.response.CustomResponse;
import com.example.harumeonglog.global.security.annotation.AuthenticatedMember;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/pets")
@Tag(name = "Pet", description = "Pet 관련 API")
public class PetController implements PetControllerSpecification {

    private final PetCommandService petCommandService;
    private final PetQueryService petQueryService;

    @PostMapping
    public CustomResponse<AddPetResponse> addPet(
            @RequestBody AddPetRequest request,
            @AuthenticatedMember Member member) {
        return CustomResponse.created(petCommandService.addPet(request, member));
    }

    @PatchMapping("/{petId}")
    public CustomResponse<ChangePetInfoResponse> updatePetInfo(
            @PathVariable Long petId,
            @RequestBody ChangePetInfoRequest request,
            @AuthenticatedMember Member member) {
        return CustomResponse.ok(petCommandService.changePetInfo(petId, request, member));
    }

    @GetMapping
    public CustomResponse<PetResponse.GetPetsResponse> getPets(
            @RequestParam(required = false) Long cursor, // 커서 (마지막 펫 ID)
            @RequestParam(defaultValue = "10") int size,  // 페이지 크기
            @AuthenticatedMember Member member) {
        return CustomResponse.ok(petQueryService.getPets(cursor, size, member));
    }

    @GetMapping("/active")
    public CustomResponse<PetResponse.PetListPreviewResponse> getActivePets(
            @RequestParam(required = false) Long cursor, // 커서 (마지막 펫 ID)
            @RequestParam(defaultValue = "10") int size,  // 페이지 크기
            @AuthenticatedMember Member member) {
        return CustomResponse.ok(petQueryService.getChangePet(cursor, size, member));
    }

    @PatchMapping("/{petId}/status/active")
    public CustomResponse<String> updateCurrentPet(
            @PathVariable Long petId,
            @AuthenticatedMember Member member) {
        petCommandService.changeCurrentPet(petId, member);
        return CustomResponse.ok("변경 완료");
    }

    @GetMapping("/active/primary")
    public CustomResponse<PetResponse.MainPetResponse> getCurrentPet(@AuthenticatedMember Member member){
        return CustomResponse.ok(petQueryService.getMainPet(member));
    }


    @DeleteMapping("/{petId}")
    public CustomResponse<String> deletePet(@PathVariable Long petId,
                                            @AuthenticatedMember Member member) {
        petCommandService.deletePet(petId, member);
        return CustomResponse.ok("펫 삭제 완료");
    }

    @PostMapping("/{petId}/members")
    public CustomResponse<String> invite(
            @PathVariable Long petId,
            @RequestBody PetRequest.InviteListRequest request,
            @AuthenticatedMember Member member) {
        petCommandService.invite(petId, request, member);
        return CustomResponse.ok("초대 완료");
    }

    @GetMapping("/members")
    public CustomResponse<PetResponse.SearchMemberResponse> searchMember(
            @RequestParam String email,
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticatedMember Member member
    ) {
        PetResponse.SearchMemberResponse result = petQueryService.searchMember(email, member, cursor, size);
        return CustomResponse.ok(result);
    }



}