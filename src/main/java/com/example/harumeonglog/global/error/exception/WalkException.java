package com.example.harumeonglog.global.error.exception;

import com.example.harumeonglog.global.error.code.WalkErrorCode;

public class WalkException extends GeneralException {

    public WalkException(WalkErrorCode code) {
        super(code);
    }
}
