package com.example.harumeonglog.global.common.service;

import com.example.harumeonglog.global.common.entity.OutBox;
import com.example.harumeonglog.global.common.entity.enums.EventType;
import com.example.harumeonglog.global.common.repository.OutBoxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class OutBoxService {
    private final OutBoxRepository outBoxRepository;

    public void saveFCMEvent(String payload) {
        OutBox outbox = OutBox.builder()
                .eventType(EventType.FCM)
                .payload(payload)
                .processed(false)
                .build();

        outBoxRepository.save(outbox);
    }
}
