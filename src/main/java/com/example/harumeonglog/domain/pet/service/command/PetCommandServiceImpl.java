package com.example.harumeonglog.domain.pet.service.command;

import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.member.entity.enums.MemberPetRole;
import com.example.harumeonglog.domain.pet.converter.MemberPetConverter;
import com.example.harumeonglog.domain.pet.converter.PetConverter;
import com.example.harumeonglog.domain.pet.dto.request.PetRequest;
import com.example.harumeonglog.domain.pet.dto.response.PetResponse;
import com.example.harumeonglog.domain.pet.entity.MemberPet;
import com.example.harumeonglog.domain.pet.entity.Pet;
import com.example.harumeonglog.domain.pet.repository.MemberPetRepository;
import com.example.harumeonglog.domain.pet.repository.PetRepository;
import com.example.harumeonglog.global.error.code.PetErrorCode;
import com.example.harumeonglog.global.error.code.S3ErrorCode;
import com.example.harumeonglog.global.error.exception.PetException;
import com.example.harumeonglog.global.error.exception.S3Exception;
import com.example.harumeonglog.global.util.S3Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class PetCommandServiceImpl implements PetCommandService {
    private final PetRepository petRepository;
    private final MemberPetRepository memberPetRepository;
    private final S3Util s3Util;

    @Override
    public PetResponse.AddPetResponse addPet(PetRequest.AddPetRequest request, MultipartFile mainImage, Member member) {
        // 유효성 검사
        if (mainImage == null || mainImage.isEmpty()) {
            throw new PetException(PetErrorCode.IMAGE_NOT_FOUND);
        }
        try {
            // Pet 엔터티 생성
            Pet pet = PetConverter.toPet(request);
            Pet savedPet = petRepository.save(pet);

            // S3 키 생성
            String mainImageKey = String.format("pet/%d/main/%s/%s",
                    pet.getId(), UUID.randomUUID(), mainImage.getOriginalFilename());

            // S3에 이미지 업로드
            s3Util.uploadFile(mainImage, mainImageKey);

            // pet에 이미지 저장
            pet.setMainImage(mainImageKey);

            // memberPet 생성
            MemberPet memberPet = MemberPetConverter.toMemberPet(member, pet, MemberPetRole.OWNER);
            memberPetRepository.save(memberPet);

            return PetConverter.toAddPetResponse(savedPet);
        } catch (IOException e) {
            throw new S3Exception(S3ErrorCode.UPLOAD_FAILED);
        } catch (S3Exception e) {
            throw new S3Exception(S3ErrorCode.SERVICE_UNAVAILABLE);
        }
    }

    @Override
    public PetResponse.ChangePetInfoResponse changePetInfo(Long petId, PetRequest.ChangePetInfoRequest request) {
        return null;
    }


    @Override
    public PetResponse.ChangeCurrentPetResponse changeCurrentPet(PetRequest.ChangeCurrentPetRequest request) {
        return null;
    }

    @Override
    public void deletePet(Long petId) {

    }

    @Override
    public void invite(Long petId, PetRequest.InviteRequest request) {

    }

}
