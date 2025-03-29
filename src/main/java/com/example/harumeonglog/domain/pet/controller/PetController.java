package com.example.harumeonglog.domain.pet.controller;


import com.example.harumeonglog.domain.common.controller.response.CustomResponse;
import com.example.harumeonglog.domain.pet.controller.dto.request.PetRequest.AddPetRequest;
import com.example.harumeonglog.domain.pet.controller.dto.request.PetRequest.ChangeCurrentPetRequest;
import com.example.harumeonglog.domain.pet.controller.dto.request.PetRequest.ChangePetInfoRequest;
import com.example.harumeonglog.domain.pet.controller.dto.request.PetRequest.InviteRequest;
import com.example.harumeonglog.domain.pet.controller.dto.response.PetResponse.*;
import com.example.harumeonglog.domain.pet.controller.port.PetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/pets")
public class PetController {

    private final PetService petService;

    @PostMapping
    public ResponseEntity<CustomResponse<AddPetResponse>> addPet(@RequestBody AddPetRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(CustomResponse.created(petService.addPet(request)));
    }

    @PutMapping("/{petId}")
    public CustomResponse<ChangePetInfoResponse> changePetInfo(
            @PathVariable Long petId, @RequestBody ChangePetInfoRequest request) {
        return CustomResponse.ok(petService.changePetInfo(petId, request));
    }

    @GetMapping
    public CustomResponse<GetPetsResponse> getPets(
            @RequestParam(required = false) Long cursor, // 커서 (마지막 펫 ID)
            @RequestParam(defaultValue = "10") int size) { // 페이지 크기
        return CustomResponse.ok(petService.getPets(cursor, size));
    }

    @PatchMapping("/current")
    public CustomResponse<ChangeCurrentPetResponse> changeCurrentPet(
            @RequestBody ChangeCurrentPetRequest request) {
        return CustomResponse.ok(petService.changeCurrentPet(request));
    }

    @PatchMapping("/{petId}")
    public CustomResponse<String> deletePet(@PathVariable Long petId) {
        petService.deletePet(petId);
        return CustomResponse.ok("펫 삭제 완료");
    }

    @PostMapping("/{petId}/invite")
    public CustomResponse<String> invite(
            @PathVariable Long petId, @RequestBody InviteRequest request) {
        petService.invite(petId, request);
        return CustomResponse.ok("초대 완료");
    }

    @GetMapping("/search-member")
    public CustomResponse<SearchMemberResponse> searchMember(
            @RequestParam String email,
            @RequestParam(required = false) Long cursor, // 커서 (마지막 펫 ID)
            @RequestParam(defaultValue = "10") int size
    ) {
        SearchMemberResponse result = petService.searchMember(email, cursor, size);
        return CustomResponse.ok(result);
    }

}