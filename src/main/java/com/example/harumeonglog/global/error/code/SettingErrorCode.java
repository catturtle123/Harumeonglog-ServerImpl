package com.example.harumeonglog.global.error.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SettingErrorCode implements BaseErrorCode{

    SETTING_NOT_FOUND(HttpStatus.NOT_FOUND, "SETTING404", "사용자의 설정을 찾지 못했습니다."),
    ;
    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
