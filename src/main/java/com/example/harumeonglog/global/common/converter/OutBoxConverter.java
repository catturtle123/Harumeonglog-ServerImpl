package com.example.harumeonglog.global.common.converter;

import com.example.harumeonglog.global.common.entity.OutBox;
import com.example.harumeonglog.global.common.entity.enums.EventType;

public class OutBoxConverter {

    public static OutBox toFCMOutBox(String payload) {
        return OutBox.builder()
                    .eventType(EventType.FCM)
                    .payload(payload)
                    .processed(false)
                    .build();
    }
}
