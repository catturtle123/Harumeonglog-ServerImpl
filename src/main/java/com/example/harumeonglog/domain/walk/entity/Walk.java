package com.example.harumeonglog.domain.walk.entity;

import com.example.harumeonglog.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "walk")
public class Walk extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "walk_id")
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "distance", nullable = false)
    private Double distance;

    @Column(name = "time", nullable = false)
    private Integer time;

    @Column(name = "start_latitude", nullable = false)
    private Double startLatitude;

    @Column(name = "start_longitude", nullable = false)
    private Double startLongitude;

    @Column(name = "walk_like_num", nullable = false)
    private Long walkLikeNum;

    @Column(name = "is_shared", nullable = false)
    private Boolean isShared;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}
