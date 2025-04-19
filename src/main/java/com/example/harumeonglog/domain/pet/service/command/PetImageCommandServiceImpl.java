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
import com.example.harumeonglog.global.util.S3Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PetImageCommandServiceImpl implements PetImageCommandService {
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
    public List<Map<String, String>> addImagesPresignedUrl(Member member, PetImageRequest.AddImagesPresignedUrlRequest images) {

//        Pet pet = findPetById(images.getPetId());
//        validateOwnerAccess(member, pet);

        return IntStream.range(0, images.getFilenames().size())
                .mapToObj(i -> uploadImage(images.getFilenames().get(i), images.getContentTypes().get(i), images.getPetId()))
                .toList();
    }

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

        return PetImageConverter.toAddImagesResponse(imageKeys);
    }

    @Override
    public void deleteImage(Long imageId, Member member) {
        PetImage petImage = petImageRepository.findById(imageId).orElseThrow(
                () -> new PetException(PetErrorCode.IMAGE_NOT_FOUND));

        Pet pet = findPetById(petImage.getPet().getId());
        validateOwnerAccess(member, pet);

        s3Util.deleteFile(petImage.getImageKey());
        petImageRepository.delete(petImage);
    }

    @Override
    public void deleteImages(Long petId, PetImageRequest.DeleteImagesRequest request, Member member) {
        List<PetImage> petImages = petImageRepository.findByIdInAndPetId(request.getImageIds(), petId);

        Pet pet = findPetById(petId);
        validateOwnerAccess(member, pet);

        // S3 파일 삭제 및 DB 삭제
        petImages.forEach(image -> s3Util.deleteFile(image.getImageKey()));
        petImageRepository.deleteAllByIdIn(request.getImageIds());
    }

    // 이미지 S3 업로드
    @Override
    public Map<String, String> uploadImage(String filename, String contentType, Long petId){
        String uuid = UUID.randomUUID().toString();
        String imageKey;

        if (petId == null) {
            // Pet 객체 미생성: 임시 업로드
            imageKey = String.format("pet/temp/%s/%s", uuid, filename);
        } else {
            // Pet 객체 존재: 특정 Pet에 연결
            imageKey = String.format("pet/%d/%s/%s",  petId, uuid, filename);
        }

        // Presigned URL 생성
        String presignedUrl = s3Util.generatePresignedUrlForUpload(
                imageKey,
                contentType,
                -1, // 클라이언트에서 ContentLength를 지정하도록 함
                10); // 10분 유효
        Map<String, String> response = new HashMap<>();
        response.put("presignedUrl", presignedUrl);
        response.put("imageKey", imageKey);

        return response;
    }

}
