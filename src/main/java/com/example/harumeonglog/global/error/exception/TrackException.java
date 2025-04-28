package com.example.harumeonglog.global.error.exception;

import com.example.harumeonglog.global.error.code.TrackErrorCode;

public class TrackException extends GeneralException {

    public TrackException(TrackErrorCode code) {
        super(code);
    }
}
