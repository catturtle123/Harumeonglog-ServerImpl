package com.example.harumeonglog.domain.event.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

//  기타 일정

@Getter
@SuperBuilder
public class GeneralEvent extends Event {
    private String details;
}