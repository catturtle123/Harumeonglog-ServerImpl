package com.example.harumeonglog.global.error.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum JsonDeserializationErrorCode implements BaseErrorCode {
    _JSON_DESERIALIZATION_FAILED(HttpStatus.BAD_REQUEST, "COMMON400", "JSON 역직렬화에 실패했습니다.")
    ;


    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
