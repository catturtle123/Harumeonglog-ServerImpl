package com.example.harumeonglog.domain.member.domain;


import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class Setting {

    private Long id;

    private Boolean morningAlarm;

    private Boolean eventAlarm;

    private Boolean articleLikeAlarm;

    private Boolean commentAlarm;

    private Member member;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
