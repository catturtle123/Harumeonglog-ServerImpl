package com.example.harumeonglog.global.error.exception;

import com.example.harumeonglog.global.error.code.BaseErrorCode;

public class EventException extends GeneralException {
    public EventException(BaseErrorCode baseErrorCode) {
        super(baseErrorCode);
    }
}
