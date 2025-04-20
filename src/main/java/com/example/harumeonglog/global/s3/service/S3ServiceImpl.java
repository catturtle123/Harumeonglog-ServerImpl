package com.example.harumeonglog.global.s3.service;

import com.example.harumeonglog.global.s3.dto.S3RequestDTO;
import com.example.harumeonglog.global.s3.enums.S3Domain;
import com.example.harumeonglog.global.util.S3Util;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class S3ServiceImpl implements S3Service {

    private final S3Util s3Util;

    @Override
    public Map<String, String> generatePresignedUrl(String filename, String contentType, S3Domain domain, Long entityId) {
        String uuid = UUID.randomUUID().toString();
        String imageKey;

        if (entityId == null) {
            // 임시 저장소 경로
            imageKey = String.format("%s/temp/%s/%s", domain.name().toLowerCase(), uuid, filename);
        } else {
            // 특정 엔티티에 연결된 경로
            imageKey = String.format("%s/%d/%s/%s", domain.name().toLowerCase(), entityId, uuid, filename);
        }

        // Presigned URL 생성
        String presignedUrl = s3Util.generatePresignedUrlForUpload(
                imageKey,
                contentType,
                -1, // 클라이언트에서 ContentLength를 지정하도록 함
                3); // 10분 유효

        Map<String, String> response = new HashMap<>();
        response.put("presignedUrl", presignedUrl);
        response.put("imageKey", imageKey);

        return response;
    }

    @Override
    public List<Map<String, String>> generatePresignedUrls(S3RequestDTO.GeneratePresignedUrlsRequest request) {
        return IntStream.range(0, request.getFilenames().size())
                .mapToObj(i -> generatePresignedUrl(
                        request.getFilenames().get(i),
                        request.getContentTypes().get(i),
                        request.getDomain(),
                        request.getEntityId()))
                .toList();
    }
}