package com.example.harumeonglog.global.common.service;

import com.example.harumeonglog.global.common.entity.OutBox;
import com.example.harumeonglog.global.common.entity.enums.EventType;

import java.util.List;

public interface OutBoxService {
    void saveFCMEvent(String payload);
    List<OutBox>  findTop100(Integer maxRetryCount, EventType eventType, Integer batchSize);
}
