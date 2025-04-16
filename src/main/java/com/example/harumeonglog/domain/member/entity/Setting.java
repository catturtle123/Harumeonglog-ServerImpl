package com.example.harumeonglog.domain.member.entity;

import com.example.harumeonglog.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "setting")
public class Setting extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "setting_id")
    private Long id;

    @Column(name = "morning_alarm", nullable = false)
    private Boolean morningAlarm;

    @Column(name = "event_alarm", nullable = false)
    private Boolean eventAlarm;

    @Column(name = "article_like_alarm", nullable = false)
    private Boolean articleLikeAlarm;

    @Column(name = "comment_alarm", nullable = false)
    private Boolean commentAlarm;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;
}
