package com.example.harumeonglog.global.error.exception;

import com.example.harumeonglog.global.error.code.AuthErrorCode;

public class AuthException extends GeneralException {

    public AuthException(AuthErrorCode code) {
        super(code);
    }
}
