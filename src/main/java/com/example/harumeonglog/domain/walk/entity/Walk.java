package com.example.harumeonglog.domain.walk.entity;

import com.example.harumeonglog.domain.walk.entity.enums.WalkStatus;
import com.example.harumeonglog.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @Column(name = "title")
    private String title;

    @Column(name = "distance")
    @Builder.Default
    private Double distance = 0.0;

    @Column(name = "time")
    @Builder.Default
    private Long time = 0L;

    @Column(name = "start_latitude", nullable = false)
    private Double startLatitude;

    @Column(name = "start_longitude", nullable = false)
    private Double startLongitude;

    @Column(name = "walk_like_num", nullable = false)
    @Builder.Default
    private Long walkLikeNum = 0L;

    @Column(name = "is_shared", nullable = false)
    @Builder.Default
    private Boolean isShared = false;

    @Column(name = "walk_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private WalkStatus status;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @OneToMany(mappedBy = "walk", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Track> trackList = new ArrayList<>();

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateWalkStatus(WalkStatus walkStatus) {
        this.status = walkStatus;
    }

    public void invertShare() {
        this.isShared = !isShared;
    }

    public void changeLikeNum(Long likeNum) {
        this.walkLikeNum += likeNum;
    }

    public void updateDistance(Double distance) {
        this.distance = distance;
    }

    public void updateTime(long time) {
        this.time = time;
    }
}
