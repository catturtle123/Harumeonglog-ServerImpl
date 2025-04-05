package com.example.harumeonglog.domain.common.auth.exception;

import com.example.harumeonglog.domain.common.controller.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AuthErrorCode implements BaseErrorCode {
    FAIL_LOGOUT(HttpStatus.BAD_REQUEST, "AUTH400", "로그아웃에 실패했습니다."),
    FAIL_AUTHENTICATION(HttpStatus.UNAUTHORIZED, "AUTH401", "인증에 실패했습니다."),
    FAIL_AUTHORIZATION(HttpStatus.FORBIDDEN, "AUTH403", "인가에 실패했습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
