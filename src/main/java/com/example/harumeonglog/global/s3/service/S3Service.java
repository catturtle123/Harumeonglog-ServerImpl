package com.example.harumeonglog.global.s3.service;

import com.example.harumeonglog.global.s3.dto.request.S3RequestDTO;
import com.example.harumeonglog.global.s3.dto.response.S3ResponseDTO;

public interface S3Service {
    S3ResponseDTO.S3ResponseListDTO generatePresignedUrls(S3RequestDTO.GeneratePresignedUrlsRequest request);
}