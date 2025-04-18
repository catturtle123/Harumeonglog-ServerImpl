package com.example.harumeonglog.global.error.exception;

import com.example.harumeonglog.global.error.code.BaseErrorCode;

public class NoticeException extends GeneralException {
    public NoticeException(BaseErrorCode baseErrorCode) {
        super(baseErrorCode);
    }
}
