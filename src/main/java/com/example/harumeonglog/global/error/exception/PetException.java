package com.example.harumeonglog.global.error.exception;

import com.example.harumeonglog.global.error.code.BaseErrorCode;

public class PetException extends GeneralException{
    public PetException(BaseErrorCode baseErrorCode) {
        super(baseErrorCode);
    }
}
