package com.navesh.notifyx.gateway;

import com.navesh.notifyx.model.SmsPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class MockSmsGateway implements SmsGateway {

    private static final Logger log =
            LoggerFactory.getLogger(MockSmsGateway.class);

    @Override
    public void send(SmsPayload payload) {
        log.info("========== MOCK SMS ==========");
        log.info("To      : {}", payload.phoneNumber());
        log.info("Message : {}", payload.message());
        log.info("==============================");
    }
}
