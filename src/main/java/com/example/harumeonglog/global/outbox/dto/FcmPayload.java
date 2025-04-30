package com.example.harumeonglog.global.outbox.dto;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FcmPayload {
    private Long receiverId;
    private String title;
    private String body;
    private String noticeType;
}
