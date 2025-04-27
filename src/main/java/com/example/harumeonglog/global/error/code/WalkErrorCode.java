package com.example.harumeonglog.global.error.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum WalkErrorCode implements BaseErrorCode {
    NOT_FOUND(HttpStatus.NOT_FOUND, "WALK404", "해당 산책을 찾을 수 없습니다."),
    UNAVAILABLE_WALK(HttpStatus.BAD_REQUEST, "WALK400", "해당 펫과 산책이 불가능합니다."),
    CANNOT_CHANGE_STATUS(HttpStatus.BAD_REQUEST, "WALK400", "해당 상태로 변경할 수 없습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
