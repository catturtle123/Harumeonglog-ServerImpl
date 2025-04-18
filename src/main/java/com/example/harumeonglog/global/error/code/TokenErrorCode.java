package com.example.harumeonglog.global.error.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum TokenErrorCode implements BaseErrorCode {
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "TOKEN401", "토큰 기한이 만료되었습니다."),
    INVALID_ID_TOKEN(HttpStatus.BAD_REQUEST, "TOKEN400", "id_token이 유효하지 않습니다."),
    FAIL_PARSING_ID_TOKEN(HttpStatus.BAD_REQUEST, "TOKEN400", "id_token parsing에 실패했습니다."),
    INVALID_TOKEN(HttpStatus.BAD_REQUEST, "TOKEN400", "토큰이 유효하지 않습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
