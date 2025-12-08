package com.example.harumeonglog.global.firebase.dto.request;

import lombok.Builder;
import lombok.Getter;

public class FCMSendRequest {

    @Getter
    @Builder
    public static class ReceiverRequest {
        private Long receiverId;
        private String deviceId;
    }
}
