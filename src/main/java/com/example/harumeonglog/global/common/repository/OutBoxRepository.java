package com.example.harumeonglog.global.common.repository;

import com.example.harumeonglog.global.common.entity.OutBox;
import com.example.harumeonglog.global.common.entity.enums.EventType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OutBoxRepository extends JpaRepository<OutBox, Long> {

    @Query("select o from OutBox o where o.eventType = :eventType and o.retryCount < 3 order by o.createdAt asc")
    List<OutBox> findTopOutBox(Integer retryCount, EventType eventType, Pageable pageable);
}
