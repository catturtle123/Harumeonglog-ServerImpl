package com.example.harumeonglog.global.error.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum TrackErrorCode implements BaseErrorCode {
    NOT_FOUND(HttpStatus.NOT_FOUND, "TRACK404", "산책 경로를 찾지 못했습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
