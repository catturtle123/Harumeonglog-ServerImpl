package com.example.harumeonglog.global.s3.service;

import com.example.harumeonglog.global.error.code.S3ErrorCode;
import com.example.harumeonglog.global.error.exception.S3Exception;
import com.example.harumeonglog.global.outbox.converter.OutBoxConverter;
import com.example.harumeonglog.global.outbox.entity.OutBox;
import com.example.harumeonglog.global.outbox.repository.OutBoxRepository;
import com.example.harumeonglog.global.s3.converter.S3Converter;
import com.example.harumeonglog.global.s3.dto.request.S3RequestDTO;
import com.example.harumeonglog.global.s3.dto.response.S3ResponseDTO;
import com.example.harumeonglog.global.s3.enums.S3Domain;
import com.example.harumeonglog.global.util.S3Util;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class S3ServiceImpl implements S3Service {

    private final S3Util s3Util;
    private final OutBoxRepository outBoxRepository;

    private List<String> acceptImageType = List.of("image/jpeg", "image/png", "image/gif", "image/jpg");

    @Override
    public S3ResponseDTO.S3ResponseListDTO generatePresignedUrls(S3RequestDTO.GeneratePresignedUrlsRequest request) {
        List<S3RequestDTO.EntityImageRequest> entityImageRequests = request.getEntities();

        if (entityImageRequests == null || entityImageRequests.isEmpty()) {
            throw new S3Exception(S3ErrorCode.EMPTY_ENTITY_IMAGE_REQUEST);
        }

        List<S3ResponseDTO.EntityImageResponse> entityResponses = entityImageRequests.stream()
                .map(entityRequest -> {

                    if (entityRequest.getImages() == null || entityRequest.getImages().isEmpty()) {
                        throw new S3Exception(S3ErrorCode.EMPTY_IMAGE_LIST);
                    }

                    List<S3ResponseDTO.S3ResponsePreviewDTO> imageList = entityRequest.getImages().stream()
                            .map(image -> {
                                // 이미지 타입 검증
                                if (!acceptImageType.contains(image.getContentType())) {
                                    throw new S3Exception(S3ErrorCode.INVALID_TYPE);
                                }

                                // imageKey 생성
                                String imageKey = generateImageKey(
                                        entityRequest.getEntityId(),
                                        request.getDomain(),
                                        image.getFilename()
                                );

                                // Presigned URL 생성
                                String presignedUrl = generatePresignedUrl(imageKey, image.getContentType());

                                // Outbox 저장
                                OutBox outBox = OutBoxConverter.toS3OutBox(imageKey);
                                outBoxRepository.save(outBox);

                                return S3Converter.toS3ResponsePreviewDTO(presignedUrl, imageKey);
                            })
                            .collect(Collectors.toList());

                    return S3Converter.toEntityImageResponse(entityRequest.getEntityId(), imageList);
                })
                .collect(Collectors.toList());

        return S3Converter.toS3ResponseDTO(entityResponses);
    }



    private String generateImageKey(Long entityId, S3Domain domain, String filename) {
        String uuid = UUID.randomUUID().toString();
        String imageKey;

        if (entityId == null) {
            // 임시 저장소 경로
            imageKey = String.format("%s/temp/%s/%s", domain.name().toLowerCase(), uuid, filename);
        } else {
            // 특정 엔티티에 연결된 경로
            imageKey = String.format("%s/%d/%s/%s", domain.name().toLowerCase(), entityId, uuid, filename);
        }
        return imageKey;
    }

    private String generatePresignedUrl(String imageKey, String contentType) {
        return s3Util.generatePresignedUrlForUpload(
                imageKey,
                contentType,
                -1,
                3);
    }
}