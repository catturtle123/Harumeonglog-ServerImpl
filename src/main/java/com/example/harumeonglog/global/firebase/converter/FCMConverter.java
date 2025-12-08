package com.example.harumeonglog.global.firebase.converter;

import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.global.firebase.dto.request.FCMSendRequest.ReceiverRequest;

public class FCMConverter {

    public static ReceiverRequest toReceiverRequest(Member member) {
        return ReceiverRequest.builder()
                .receiverId(member.getId())
                .deviceId(member.getDeviceId())
                .build();
    }

}
