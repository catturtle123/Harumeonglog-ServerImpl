package com.example.harumeonglog.domain.pet.controller;


import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.pet.controller.specification.PetControllerSpecification;
import com.example.harumeonglog.domain.pet.dto.response.PetResponse;
import com.example.harumeonglog.domain.pet.dto.response.PetResponse.AddPetResponse;
import com.example.harumeonglog.domain.pet.dto.response.PetResponse.ChangePetInfoResponse;
import com.example.harumeonglog.domain.pet.service.query.PetQueryService;
import com.example.harumeonglog.global.common.response.CustomResponse;
import com.example.harumeonglog.domain.pet.dto.request.PetRequest.AddPetRequest;
import com.example.harumeonglog.domain.pet.dto.request.PetRequest.ChangeCurrentPetRequest;
import com.example.harumeonglog.domain.pet.dto.request.PetRequest.ChangePetInfoRequest;
import com.example.harumeonglog.domain.pet.dto.request.PetRequest.InviteRequest;
import com.example.harumeonglog.domain.pet.service.command.PetCommandService;
import com.example.harumeonglog.global.security.annotation.AuthenticatedMember;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/pets")
@Tag(name = "Pet", description = "Pet 관련 API")
public class PetController implements PetControllerSpecification {

    private final PetCommandService petCommandService;
    private final PetQueryService petQueryService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomResponse<AddPetResponse>> addPet(@RequestPart("mainImage") MultipartFile mainImage,
                                                                 @RequestPart AddPetRequest request,
                                                                 @AuthenticatedMember Member member) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(CustomResponse.created(petCommandService.addPet(request, mainImage, member)));
    }

    @PutMapping("/{petId}")
    public CustomResponse<ChangePetInfoResponse> changePetInfo(
            @PathVariable Long petId, @RequestBody ChangePetInfoRequest request) {
        return CustomResponse.ok(petCommandService.changePetInfo(petId, request));
    }

    @GetMapping
    public CustomResponse<PetResponse.GetPetsResponse> getPets(
            @RequestParam(required = false) Long cursor, // 커서 (마지막 펫 ID)
            @RequestParam(defaultValue = "10") int size) { // 페이지 크기
        return CustomResponse.ok(petQueryService.getPets(cursor, size));
    }

    @PatchMapping("/current")
    public CustomResponse<PetResponse.ChangeCurrentPetResponse> changeCurrentPet(
            @RequestBody ChangeCurrentPetRequest request) {
        return CustomResponse.ok(petCommandService.changeCurrentPet(request));
    }

    @PatchMapping("/{petId}")
    public CustomResponse<String> deletePet(@PathVariable Long petId) {
        petCommandService.deletePet(petId);
        return CustomResponse.ok("펫 삭제 완료");
    }

    @PostMapping("/{petId}/invite")
    public CustomResponse<String> invite(
            @PathVariable Long petId, @RequestBody InviteRequest request) {
        petCommandService.invite(petId, request);
        return CustomResponse.ok("초대 완료");
    }

    @GetMapping("/search-member")
    public CustomResponse<PetResponse.SearchMemberResponse> searchMember(
            @RequestParam String email,
            @RequestParam(required = false) Long cursor, // 커서 (마지막 펫 ID)
            @RequestParam(defaultValue = "10") int size
    ) {
        PetResponse.SearchMemberResponse result = petQueryService.searchMember(email, cursor, size);
        return CustomResponse.ok(result);
    }

}