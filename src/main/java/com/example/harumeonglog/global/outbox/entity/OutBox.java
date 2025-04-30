package com.example.harumeonglog.global.outbox.entity;

import com.example.harumeonglog.global.common.entity.BaseEntity;
import com.example.harumeonglog.global.outbox.entity.enums.EventType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "out_box")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OutBox extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "out_box_id")
    private Long id;

    @Column(name = "event_type", nullable = false)
    private EventType eventType;

    @Lob
    @Column(name = "payload", nullable = false, columnDefinition = "TEXT")
    private String payload;

    @Column(name = "processed", nullable = false)
    private Boolean processed;

    @Column(name = "retry_count", nullable = false)
    @Builder.Default
    private Integer retryCount = 0;

    public void markProcessed() {
        this.processed = true;
    }

    public void increaseRetryCount() {
        this.retryCount++;
    }
}

