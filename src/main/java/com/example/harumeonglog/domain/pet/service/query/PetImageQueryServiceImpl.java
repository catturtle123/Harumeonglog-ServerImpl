package com.example.harumeonglog.domain.pet.service.query;

import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.member.entity.enums.MemberPetRole;
import com.example.harumeonglog.domain.pet.converter.PetImageConverter;
import com.example.harumeonglog.domain.pet.dto.response.PetImageResponse;
import com.example.harumeonglog.domain.pet.entity.MemberPet;
import com.example.harumeonglog.domain.pet.entity.Pet;
import com.example.harumeonglog.domain.pet.entity.PetImage;
import com.example.harumeonglog.domain.pet.repository.MemberPetRepository;
import com.example.harumeonglog.domain.pet.repository.PetImageRepository;
import com.example.harumeonglog.domain.pet.repository.PetRepository;
import com.example.harumeonglog.global.error.code.PetErrorCode;
import com.example.harumeonglog.global.error.exception.PetException;
import com.example.harumeonglog.global.util.S3Util;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PetImageQueryServiceImpl implements PetImageQueryService {
    private final PetImageRepository petImageRepository;
    private final PetRepository petRepository;
    private final MemberPetRepository memberPetRepository;
    private final S3Util s3Util;


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

    @Override
    public PetImageResponse.GetImagesResponse getImages(Long petId, Member member, Long cursor, int size) {
        Pet pet = findPetById(petId);
        validateOwnerAccess(member, pet);

        Pageable pageable = PageRequest.of(0, size);
        Slice<PetImage> imageSlice;

        if (cursor == null || cursor == 0L) {
            imageSlice = petImageRepository.findByPetId(petId, pageable);
        } else {
            imageSlice = petImageRepository.findByPetIdAndCursor(petId, cursor, pageable);
        }

        return PetImageConverter.toGetImagesResponse(imageSlice, s3Util);
    }

    @Override
    public PetImageResponse.RecentImagesResponse recentImages() {
        return null;
    }

    @Override
    public PetImageResponse.GetImageResponse getImage(Long imageId, Member member) {
        PetImage petImage = petImageRepository.findById(imageId).orElseThrow(
                () -> new PetException(PetErrorCode.IMAGE_NOT_FOUND));

        Pet pet = findPetById(petImage.getPet().getId());
        validateOwnerAccess(member, pet);

        String image = s3Util.getUrlFromKey(petImage.getImageKey());

        return PetImageConverter.toGetImageResponse(petImage, image);
    }
}
