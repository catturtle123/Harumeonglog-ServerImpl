package com.example.harumeonglog.domain.pet.service.command;

import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.member.entity.enums.MemberPetRole;
import com.example.harumeonglog.domain.pet.converter.PetImageConverter;
import com.example.harumeonglog.domain.pet.dto.request.PetImageRequest;
import com.example.harumeonglog.domain.pet.dto.response.PetImageResponse;
import com.example.harumeonglog.domain.pet.entity.MemberPet;
import com.example.harumeonglog.domain.pet.entity.Pet;
import com.example.harumeonglog.domain.pet.entity.PetImage;
import com.example.harumeonglog.domain.pet.repository.MemberPetRepository;
import com.example.harumeonglog.domain.pet.repository.PetImageRepository;
import com.example.harumeonglog.domain.pet.repository.PetRepository;
import com.example.harumeonglog.global.error.code.PetErrorCode;
import com.example.harumeonglog.global.error.exception.PetException;
import com.example.harumeonglog.global.outbox.entity.enums.EventType;
import com.example.harumeonglog.global.util.OutboxUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PetImageCommandServiceImpl implements PetImageCommandService {
    private final PetImageRepository petImageRepository;
    private final PetRepository petRepository;
    private final MemberPetRepository memberPetRepository;
    private final OutboxUtil outboxUtil;


    @Override
    public PetImageResponse.AddImagesResponse addImage(PetImageRequest.AddImageRequest request) {
        Pet pet = findPetById(request.getPetId());

        List<PetImage> petImages = request.getImageKeys().stream()
                .map(imageKey -> PetImageConverter.toPetImage(pet, imageKey))
                .toList();

        List<PetImage> savedImages = petImageRepository.saveAll(petImages);

        List<Long> imageKeys = savedImages.stream()
                .map(PetImage::getId)
                .toList();

        // Outbox 상태 변경
        request.getImageKeys().forEach(
                outboxUtil::changeS3OutboxStatus);

        LocalDateTime time = savedImages.isEmpty() ? LocalDateTime.now().withNano(0) : savedImages.get(0).getCreatedAt();


        return PetImageConverter.toAddImagesResponse(imageKeys, time);
    }

    @Override
    public void deleteImage(Long imageId, Member member) {
        PetImage petImage = petImageRepository.findById(imageId).orElseThrow(
                () -> new PetException(PetErrorCode.IMAGE_NOT_FOUND));

        Pet pet = findPetById(petImage.getPet().getId());
        validateOwnerAccess(member, pet);

        petImageRepository.delete(petImage);
    }

    @Override
    public void deleteImages(Long petId, PetImageRequest.DeleteImagesRequest request, Member member) {
        List<PetImage> petImages = petImageRepository.findByIdInAndPetId(request.getImageIds(), petId);

        Pet pet = findPetById(petId);
        validateOwnerAccess(member, pet);

        // DB 삭제
        petImageRepository.deleteAll(petImages);
    }

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
