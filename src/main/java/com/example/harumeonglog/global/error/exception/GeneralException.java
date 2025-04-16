package com.example.harumeonglog.global.error.exception;

import com.example.harumeonglog.global.error.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GeneralException extends RuntimeException {

    private final BaseErrorCode baseErrorCode;

    @Override
    public String getMessage() {
        return baseErrorCode.getMessage();
    }
}
