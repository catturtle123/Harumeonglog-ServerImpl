package com.example.harumeonglog.domain.pet.service.command;

import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.member.entity.enums.MemberPetRole;
import com.example.harumeonglog.domain.member.repository.MemberRepository;
import com.example.harumeonglog.domain.pet.converter.MemberPetConverter;
import com.example.harumeonglog.domain.pet.converter.PetConverter;
import com.example.harumeonglog.domain.pet.dto.request.PetRequest;
import com.example.harumeonglog.domain.pet.dto.response.PetResponse;
import com.example.harumeonglog.domain.pet.entity.MemberPet;
import com.example.harumeonglog.domain.pet.entity.Pet;
import com.example.harumeonglog.domain.pet.repository.MemberPetRepository;
import com.example.harumeonglog.domain.pet.repository.PetRepository;
import com.example.harumeonglog.global.error.code.MemberErrorCode;
import com.example.harumeonglog.global.error.code.PetErrorCode;
import com.example.harumeonglog.global.error.code.S3ErrorCode;
import com.example.harumeonglog.global.error.exception.MemberException;
import com.example.harumeonglog.global.error.exception.PetException;
import com.example.harumeonglog.global.error.exception.S3Exception;
import com.example.harumeonglog.global.outbox.entity.enums.EventType;
import com.example.harumeonglog.global.util.OutboxUtil;
import com.example.harumeonglog.global.util.S3Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PetCommandServiceImpl implements PetCommandService {
    private final PetRepository petRepository;
    private final MemberPetRepository memberPetRepository;
    private final MemberRepository memberRepository;
    private final S3Util s3Util;
    private final OutboxUtil outboxUtil;


    // ========== 외부 메서드 ==========


    @Override
    public PetResponse.AddPetResponse addPet(PetRequest.AddPetRequest request, Member member) {
        // 유효성 검사
        if (request.getMainImageKey() == null || request.getMainImageKey().isEmpty()) {
            throw new PetException(PetErrorCode.IMAGE_NOT_FOUND);
        }

        try {
            // Pet 엔터티 생성
            Pet pet = PetConverter.toPet(request);
            Pet savedPet = petRepository.save(pet);

            // memberPet 생성
            MemberPet memberPet = MemberPetConverter.toMemberPet(member, pet, MemberPetRole.OWNER);
            memberPetRepository.save(memberPet);

            // member의 currentPetId 지정
            member.updateCurrentPetId(savedPet.getId());
            memberRepository.save(member);

            // Outbox 상태 변경
            if(!s3Util.isObjectExists(request.getMainImageKey())) {
                throw new S3Exception(S3ErrorCode.NOT_FOUND);
            }
            outboxUtil.changeS3OutboxStatus(request.getMainImageKey());

            return PetConverter.toAddPetResponse(savedPet);
        } catch (Exception e) {
            throw new S3Exception(S3ErrorCode.SERVICE_UNAVAILABLE);
        }
    }

    @Override
    public PetResponse.ChangePetInfoResponse changePetInfo(Long petId, PetRequest.ChangePetInfoRequest request, Member member) {
        // Pet 조회
        Pet pet = findPetById(petId);

        // 권한 검증
        validateOwnerAccess(member, pet);

        try {
            String newMainImageKey = pet.getMainImage(); // 기존 키 유지

            // mainImage가 제공된 경우 처리
            if (request.getNewMainImageKey() != null && !request.getNewMainImageKey().isEmpty()) {
                // 새 이미지 키 설정
                newMainImageKey = request.getNewMainImageKey();
            }

            // Pet 정보 업데이트
            pet.update(
                    request.getName(),
                    request.getSize(),
                    request.getType(),
                    request.getGender(),
                    request.getBirth(),
                    newMainImageKey
            );

            String imageUrl = s3Util.getUrlFromKey(newMainImageKey);

            // Outbox 상태 변경
            if(!s3Util.isObjectExists(request.getNewMainImageKey())) {
                throw new S3Exception(S3ErrorCode.NOT_FOUND);
            }
            outboxUtil.changeS3OutboxStatus(request.getNewMainImageKey());

            // 응답 DTO 반환
            return PetConverter.toChangePetInfoResponse(pet, imageUrl);
        } catch (Exception e) {
            throw new S3Exception(S3ErrorCode.SERVICE_UNAVAILABLE);
        }
    }

    @Override
    public void changeCurrentPet(Long petId, Member member) {
        Pet pet = findPetById(petId);

        // 해당 MEMBER, PET 관계 확인
        memberPetRepository.findByMemberAndPet(member, pet)
                .orElseThrow(() -> new PetException(PetErrorCode.NOT_IN_GROUP));

        member.updateCurrentPetId(pet.getId());
    }

    @Override
    public void deletePet(Long petId, Member member) {
        Pet pet = findPetById(petId);

        // 권한 검증
        validateOwnerAccess(member, pet);

        // Pet soft delete
        pet.softDelete();

        // MemberPet hard delete
        memberPetRepository.deleteByPet(pet);
    }

    @Override
    public void invite(Long petId, PetRequest.InviteListRequest request, Member member) {
        Pet pet = findPetById(petId);

        // 권한 검증
        validateOwnerAccess(member, pet);

        // 초대 처리
        for (PetRequest.InviteRequest invite : request.getRequests()) {
            Member invitedMember = memberRepository.findById(invite.getMemberId())
                    .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND));

            // 이미 초대된 상태인지 확인
            if (memberPetRepository.findByMemberAndPet(invitedMember, pet).isPresent()) {
                throw new PetException(PetErrorCode.ALREADY_INVITED);
            }

            // 역할 검증 및 설정
            MemberPetRole role;
            try {
                role = MemberPetRole.valueOf(invite.getRole());
            } catch (IllegalArgumentException e) {
                throw new PetException(PetErrorCode.INVALID_ROLE);
            }

            // MemberPet 생성 및 저장
            MemberPet memberPet = MemberPetConverter.toMemberPet(invitedMember, pet, role);
            memberPetRepository.save(memberPet);
        }
    }

// ========== 내부 메서드 ==========

    // Pet 엔티티 조회
    private Pet findPetById(Long petId) {
        return petRepository.findById(petId)
                .orElseThrow(() -> new PetException(PetErrorCode.NOT_FOUND));
    }


    // 사용자 OWNER 검증
    private void validateOwnerAccess(Member member, Pet pet) {
        MemberPet memberPet = memberPetRepository.findByMemberAndPet(member, pet)
                .orElseThrow(() -> new PetException(PetErrorCode.NOT_IN_GROUP));

        if (!memberPet.getRole().equals(MemberPetRole.OWNER)) {
            throw new PetException(PetErrorCode.NOT_ALLOWED_ROLE);
        }
    }


}