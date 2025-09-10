package com.example.harumeonglog.global.util;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.example.harumeonglog.global.data.S3ConfigData;
import com.example.harumeonglog.global.error.code.S3ErrorCode;
import com.example.harumeonglog.global.error.exception.S3Exception;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.net.URL;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class S3Util {
    private final AmazonS3 amazonS3Client;
    private final S3ConfigData s3ConfigData;



    //presigned url로 파일 업로드
    @Transactional
    public String generatePresignedUrlForUpload(String key, String contentType, long contentLength, int expirationMinutes) {
        // 메타데이터 설정
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(contentType);
        metadata.setContentLength(contentLength);

        // PUT 요청을 위한 Presigned URL 생성
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(s3ConfigData.getBucket(), key)
                        .withMethod(HttpMethod.PUT)
                        .withExpiration(getExpirationDate(expirationMinutes))
                        .withContentType(contentType);

        URL presignedUrl = amazonS3Client.generatePresignedUrl(generatePresignedUrlRequest);
        return presignedUrl.toString();
    }



    // S3 파일 삭제
    public void deleteFile(String key) {
        if (key == null || key.trim().isEmpty()) {
            throw new S3Exception(S3ErrorCode.NOT_FOUND);
        }
        try {
            amazonS3Client.deleteObject(s3ConfigData.getBucket(), key);
        } catch (AmazonS3Exception e) {
            throw new S3Exception(S3ErrorCode.DELETE_FAILED);
        }
    }

    public String getUrlFromKey(String key) {
        if (key == null || key.isEmpty()) {
            return null;
        }
        return s3ConfigData.getBaseUrl() + key;
    }

    // S3 객체 존재 여부 확인
    public boolean isObjectExists(String key) {
        try {
            return amazonS3Client.doesObjectExist(s3ConfigData.getBucket(), key);
        } catch (AmazonS3Exception e) {
            throw new S3Exception(S3ErrorCode.NOT_FOUND);
        }
    }


    // Presigned URL의 만료 시간 설정
    private Date getExpirationDate(int expirationMinutes) {
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 60 * expirationMinutes; // 분 단위로 계산
        expiration.setTime(expTimeMillis);
        return expiration;
    }
}
