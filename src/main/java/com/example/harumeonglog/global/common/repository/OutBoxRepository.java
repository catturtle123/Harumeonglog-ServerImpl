package com.example.harumeonglog.global.common.repository;

import com.example.harumeonglog.global.common.entity.OutBox;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OutBoxRepository extends JpaRepository<OutBox, Long> {
    List<OutBox> findTop100ByProcessedFalseAndRetryCountLessThanOrderByCreatedAtAsc(Integer retryCount);
}
