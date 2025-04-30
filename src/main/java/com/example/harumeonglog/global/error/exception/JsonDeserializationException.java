package com.example.harumeonglog.global.error.exception;

import com.example.harumeonglog.global.error.code.JsonDeserializationErrorCode;

public class JsonDeserializationException extends GeneralException {
    public JsonDeserializationException(JsonDeserializationErrorCode code) {
        super(code);
    }
}
