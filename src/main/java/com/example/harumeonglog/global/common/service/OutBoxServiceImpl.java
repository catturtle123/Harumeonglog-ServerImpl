package com.example.harumeonglog.global.common.service;

import com.example.harumeonglog.global.common.entity.OutBox;
import com.example.harumeonglog.global.common.entity.enums.EventType;
import com.example.harumeonglog.global.common.repository.OutBoxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OutBoxServiceImpl implements OutBoxService {
    private final OutBoxRepository outBoxRepository;

    public void saveFCMEvent(String payload) {
        OutBox outbox = OutBox.builder()
                .eventType(EventType.FCM)
                .payload(payload)
                .processed(false)
                .build();

        outBoxRepository.save(outbox);
    }

    @Transactional(readOnly = true)
    public List<OutBox> findTop100(Integer maxRetryCount, EventType eventType, Integer batchSize) {
        return outBoxRepository.findTopOutBox(maxRetryCount, eventType, PageRequest.of(0, batchSize));
    }
}
