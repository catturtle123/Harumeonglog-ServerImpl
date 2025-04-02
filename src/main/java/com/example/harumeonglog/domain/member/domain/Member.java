package com.example.harumeonglog.domain.member.domain;


import com.example.harumeonglog.domain.member.domain.enums.SocialType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
public class Member {

    private Long id;

    private String email;

    private String nickname;

    private LocalDate birth;

    private SocialType socialType;

    private String image;

    private String providerId;

    private LocalDateTime deletedAt;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
