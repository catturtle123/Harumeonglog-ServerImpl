package com.example.harumeonglog.global.error.exception;

import com.example.harumeonglog.global.error.code.BaseErrorCode;

public class S3Exception extends GeneralException {

    public S3Exception(BaseErrorCode code) {
        super(code);
    }
}
