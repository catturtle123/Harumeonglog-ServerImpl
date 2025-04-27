package com.example.harumeonglog.domain.event.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "walk_event")
public class WalkEvent extends Event {

    @Column(name = "distance")
    private String distance;

    @Column(name = "duration")
    private String duration;

    @Column(name = "details")
    private String details;
}