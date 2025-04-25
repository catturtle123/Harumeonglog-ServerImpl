package com.example.harumeonglog.global.error.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum S3ErrorCode implements BaseErrorCode {

    NOT_FOUND(HttpStatus.NOT_FOUND, "S3404", "파일을 찾지 못했습니다."),
    DELETE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "S3500", "파일 삭제에 실패했습니다."),
    UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "S3501", "파일 업로드에 실패했습니다."),
    SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "S3503", "S3 서비스에 연결할 수 없습니다."),
    INVALID_TYPE(HttpStatus.BAD_REQUEST, "S3400", "잘못된 이미지 타입입니다.")
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
