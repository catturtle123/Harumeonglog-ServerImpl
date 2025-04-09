package com.example.harumeonglog.domain.event.domain;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

// 산책 이벤트

@Getter
@SuperBuilder
public class WalkEvent extends Event {
    private String distance;
    private String duration;
}