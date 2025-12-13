package com.example.harumeonglog.global.outbox.repository;

import com.example.harumeonglog.global.outbox.entity.OutBox;
import com.example.harumeonglog.global.outbox.entity.enums.EventType;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OutBoxRepository extends JpaRepository<OutBox, Long> {

    @Query("select o from OutBox o where o.eventType = :eventType and o.processed = false and o.retryCount < 3 order by o.createdAt asc")
    List<OutBox> findTopOutBox(Integer retryCount, EventType eventType, Pageable pageable);

    @Modifying
    @Query("update OutBox o set o.processed = true where o in :outBoxList")
    void updateSuccessFCMOutBox(List<OutBox> outBoxList);

    @Modifying
    @Query("update OutBox o set o.retryCount = o.retryCount + 1 where o in :failedOutBox")
    void updateFailedFCMOutBox(List<OutBox> failedOutBox);

    Optional<OutBox> findByPayloadAndEventType(String payload, EventType eventType);

    List<OutBox> findByEventTypeAndProcessedFalseAndRetryCountLessThan(EventType eventType, int maxRetryCount);}
