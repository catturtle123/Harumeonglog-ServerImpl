package com.example.harumeonglog.global.outbox.converter;

import com.example.harumeonglog.global.outbox.entity.OutBox;
import com.example.harumeonglog.global.outbox.entity.enums.EventType;

public class OutBoxConverter {

    public static OutBox toFCMOutBox(String payload) {
        return OutBox.builder()
                    .eventType(EventType.FCM)
                    .payload(payload)
                    .processed(false)
                    .build();
    }
}
