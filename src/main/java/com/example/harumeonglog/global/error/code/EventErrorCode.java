package com.example.harumeonglog.global.error.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum EventErrorCode implements BaseErrorCode {

    NOT_FOUND(HttpStatus.NOT_FOUND, "EVENT404", "일정을 찾지 못했습니다."),
    INVALID_CATEGORY(HttpStatus.BAD_REQUEST, "EVENT400", "올바르지 않은 카테고리입니다."),

    ;


    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
