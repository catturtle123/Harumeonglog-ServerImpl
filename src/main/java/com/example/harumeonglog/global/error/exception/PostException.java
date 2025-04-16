package com.example.harumeonglog.global.error.exception;

import com.example.harumeonglog.global.error.code.BaseErrorCode;

public class PostException extends GeneralException {

    public PostException(BaseErrorCode baseErrorCode) {
        super(baseErrorCode);
    }
}
