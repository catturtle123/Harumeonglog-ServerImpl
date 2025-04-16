package com.example.harumeonglog.global.error.exception;

import com.example.harumeonglog.global.error.code.MemberErrorCode;

public class MemberException extends GeneralException {
    public MemberException(MemberErrorCode code) {
        super(code);
    }
}
