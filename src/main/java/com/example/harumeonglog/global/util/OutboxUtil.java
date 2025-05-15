package com.example.harumeonglog.global.util;

import com.example.harumeonglog.global.error.code.OutboxErrorCode;
import com.example.harumeonglog.global.error.code.S3ErrorCode;
import com.example.harumeonglog.global.error.exception.OutboxException;
import com.example.harumeonglog.global.error.exception.S3Exception;
import com.example.harumeonglog.global.outbox.entity.OutBox;
import com.example.harumeonglog.global.outbox.entity.enums.EventType;
import com.example.harumeonglog.global.outbox.repository.OutBoxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OutboxUtil {
    private final OutBoxRepository outBoxRepository;
    private final S3Util s3Util;

    public void changeOutboxStatus(String payload, EventType eventType) {
        if(payload != null) {
            OutBox outBox = outBoxRepository.findByPayloadAndEventType(payload, eventType)
                    .orElseThrow(() -> new OutboxException(OutboxErrorCode.NOT_FOUND));

            outBox.markProcessed();
        }
    }

    public void changeS3OutboxStatus(String key){
        if(key != null) {
            if(!s3Util.isObjectExists(key)){
                throw new S3Exception(S3ErrorCode.NOT_FOUND);
            }
            OutBox outBox = outBoxRepository.findByPayloadAndEventType(key, EventType.S3)
                    .orElseThrow(() -> new OutboxException(OutboxErrorCode.NOT_FOUND));

            outBox.markProcessed();
        }
    }
}
