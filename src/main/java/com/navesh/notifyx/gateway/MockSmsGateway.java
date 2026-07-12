package com.navesh.notifyx.gateway;

import com.navesh.notifyx.model.SmsPayload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MockSmsGateway implements SmsGateway {

    @Override
    public void send(SmsPayload payload) {
        log.info("========== MOCK SMS ==========");
        log.info("To      : {}", payload.phoneNumber());
        log.info("Message : {}", payload.message());
        log.info("==============================");
    }
}
