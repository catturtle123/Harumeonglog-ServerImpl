package com.example.harumeonglog.global.error.exception;

import com.example.harumeonglog.global.error.code.SettingErrorCode;

public class SettingException extends GeneralException {
    public SettingException(SettingErrorCode code) {
        super(code);
    }
}
