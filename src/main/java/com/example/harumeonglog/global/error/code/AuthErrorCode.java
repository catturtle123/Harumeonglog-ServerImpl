package com.example.harumeonglog.global.error.code;

import ch.qos.logback.core.status.ErrorStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AuthErrorCode implements BaseErrorCode {
    FAIL_LOGOUT(HttpStatus.BAD_REQUEST, "AUTH400", "로그아웃에 실패했습니다."),
    FAIL_AUTHENTICATION(HttpStatus.UNAUTHORIZED, "AUTH401", "인증에 실패했습니다."),
    FAIL_AUTHORIZATION(HttpStatus.FORBIDDEN, "AUTH403", "인가에 실패했습니다."),
    UNSUPPORTED_PROVIDER(HttpStatus.BAD_REQUEST, "AUTH400", "지원하지 않는 소셜 로그인입니다."),
    INVALID_ISSUER(HttpStatus.BAD_REQUEST, "AUTH400", "발급된 곳이 유효하지 않습니다."),
    FCM_ERROR(HttpStatus.UNAUTHORIZED, "AUTH401", "해당 FCM이 유효하지 않습니다.")
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
