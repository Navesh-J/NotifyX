package com.navesh.notifyx.gateway;

import com.navesh.notifyx.model.PushPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class MockPushGateway implements PushGateway {

    private static final Logger log =
            LoggerFactory.getLogger(MockPushGateway.class);

    @Override
    public void send(PushPayload payload) {
        log.info("========== MOCK PUSH ==========");
        log.info("Device : {}", payload.deviceToken());
        log.info("Title  : {}", payload.title());
        log.info("Body   : {}", payload.body());
        log.info("==============================");
    }
}
