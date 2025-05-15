package com.example.harumeonglog.global.outbox.service;

import com.example.harumeonglog.global.outbox.converter.OutBoxConverter;
import com.example.harumeonglog.global.outbox.entity.OutBox;
import com.example.harumeonglog.global.outbox.entity.enums.EventType;
import com.example.harumeonglog.global.outbox.repository.OutBoxRepository;
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

    @Override
    public void saveFCMEvent(String payload) {
        OutBox outbox = OutBoxConverter.toFCMOutBox(payload);

        outBoxRepository.save(outbox);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OutBox> findTop100(Integer maxRetryCount, EventType eventType, Integer batchSize) {
        return outBoxRepository.findTopOutBox(maxRetryCount, eventType, PageRequest.of(0, batchSize));
    }


    @Override
    public void updateSuccessFCMOutBox(List<OutBox> outBoxList) {
        outBoxRepository.updateSuccessFCMOutBox(outBoxList);
    }

    @Override
    public void updateFailedFCMOutBox(List<OutBox> failedOutBox) {
        outBoxRepository.updateFailedFCMOutBox(failedOutBox);
    }

}
