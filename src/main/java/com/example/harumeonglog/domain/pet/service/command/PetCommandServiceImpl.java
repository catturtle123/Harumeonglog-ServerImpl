package com.example.harumeonglog.domain.pet.service.command;

import com.example.harumeonglog.domain.member.converter.InvitationConverter;
import com.example.harumeonglog.domain.member.entity.Invitation;
import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.member.entity.enums.MemberPetRole;
import com.example.harumeonglog.domain.member.entity.enums.NoticeType;
import com.example.harumeonglog.domain.member.repository.InvitationRepository;
import com.example.harumeonglog.domain.member.repository.MemberRepository;
import com.example.harumeonglog.domain.member.service.NoticeCommandService;
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
import com.example.harumeonglog.global.firebase.converter.FCMConverter;
import com.example.harumeonglog.global.firebase.service.FcmService;
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
    private final InvitationRepository invitationRepository;
    private final FcmService fcmService;
    private final NoticeCommandService noticeCommandService;

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
            if (request.getNewMainImageKey() != null && !request.getNewMainImageKey().isEmpty()) {
                if (!s3Util.isObjectExists(request.getNewMainImageKey())) {
                    throw new S3Exception(S3ErrorCode.NOT_FOUND);
                }
                outboxUtil.changeS3OutboxStatus(request.getNewMainImageKey());
            }


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
        memberRepository.save(member);
    }

    @Override
    public void deletePet(Long petId, Long memberId) {
        Pet pet = findPetById(petId);

        //삭제할 member
        Member deleteMember = memberRepository.findById(memberId).orElseThrow(
                () -> new MemberException(MemberErrorCode.NOT_FOUND));

        MemberPet deleteMemberPet = memberPetRepository.findByMemberAndPet(deleteMember, pet).orElseThrow(
                () -> new PetException(PetErrorCode.NOT_IN_GROUP));

        // 펫에 아무도 없을 경우 조건 확인
        boolean lastMember = memberPetRepository.countByPet(pet) == 1;
        boolean noOwner = memberPetRepository.countByPetAndRole(pet, MemberPetRole.OWNER) == 0;

        // 아무도 없을 경우 분기
        if (lastMember || noOwner) {
            pet.softDelete();
            memberPetRepository.deleteAllByPet(pet);
        } else {
            memberPetRepository.delete(deleteMemberPet);
        }
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

            // 1. 이미 멤버로 가입된 상태인지 확인
            if (memberPetRepository.findByMemberAndPet(invitedMember, pet).isPresent()) {
                throw new PetException(PetErrorCode.ALREADY_MEMBER);
            }

            // 2. 이미 초대가 발송된 상태인지 확인
            if (invitationRepository.findByPetAndReceiver(pet, invitedMember).isPresent()) {
                throw new PetException(PetErrorCode.ALREADY_INVITED);
            }

            // 역할 검증 및 설정
            MemberPetRole role;
            try {
                role = MemberPetRole.valueOf(invite.getRole());
            } catch (IllegalArgumentException e) {
                throw new PetException(PetErrorCode.INVALID_ROLE);
            }

            Invitation invitation = InvitationConverter.toInvitation(pet, role, member, invitedMember);
            invitationRepository.save(invitation);

            // 알림
            String title = "[초대 알림]";
            String body = String.format("%s에 초대되었습니다.", pet.getName());

            fcmService.sendPushNotification(FCMConverter.toReceiverRequest(invitedMember), title, body, NoticeType.NOTICE);
            noticeCommandService.createNotice(title, body, NoticeType.NOTICE, member, invitedMember);
        }
    }

    @Override
    public void responseInvite(Long petId, PetRequest.InviteResponseRequest request, Member member) {
        // 펫 존재 확인
        Pet pet = findPetById(petId);

        // 초대 존재 확인
        Invitation invitation = invitationRepository.findByPetAndReceiver(pet, member)
                .orElseThrow(() -> new PetException(PetErrorCode.INVITATION_NOT_FOUND));

        // 초대 응답 처리
        if (request.getResponse().equalsIgnoreCase("ACCEPT")) {
            // 수락: MemberPet 생성
            MemberPet memberPet = MemberPetConverter.toMemberPet(
                    invitation.getReceiver(),
                    pet,
                    invitation.getRole()
            );
            memberPetRepository.save(memberPet);
            invitationRepository.delete(invitation);
        } else if (request.getResponse().equalsIgnoreCase("REJECT")) {
            // 초대 삭제
            invitationRepository.delete(invitation);
        }
        else{
            throw new PetException(PetErrorCode.INVALID_RESPONSE_TYPE);
        }
    }

// ========== 내부 메서드 ==========

    // Pet 엔티티 조회
    private Pet findPetById(Long petId) {
        return petRepository.findByIdAndDeletedAtIsNull(petId)
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