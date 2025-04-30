package com.example.harumeonglog.global.outbox.service;

import com.example.harumeonglog.global.outbox.entity.OutBox;
import com.example.harumeonglog.global.outbox.entity.enums.EventType;

import java.util.List;

public interface OutBoxService {
    void saveFCMEvent(String payload);
    List<OutBox>  findTop100(Integer maxRetryCount, EventType eventType, Integer batchSize);
    void updateSuccessFCMOutBox(List<OutBox> outBoxList);

    void updateFailedFCMOutBox(List<OutBox> failedOutBox);
}
