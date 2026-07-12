package com.navesh.notifyx.gateway;

import com.navesh.notifyx.model.PushPayload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MockPushGateway implements PushGateway {

    @Override
    public void send(PushPayload payload) {
        log.info("========== MOCK PUSH ==========");
        log.info("Device : {}", payload.deviceToken());
        log.info("Title  : {}", payload.title());
        log.info("Body   : {}", payload.body());
        log.info("==============================");
    }
}
