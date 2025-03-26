package com.example.harumeonglog.domain.member.domain;


import com.example.harumeonglog.domain.member.domain.enums.NoticeType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class Notice {

    private Long id;

    private String title;

    private String content;

    private NoticeType noticeType;

    private Long targetId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
