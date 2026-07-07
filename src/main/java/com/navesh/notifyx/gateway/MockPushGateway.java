package com.navesh.notifyx.gateway;

import com.navesh.notifyx.model.PushPayload;
import org.springframework.stereotype.Component;

@Component
public class MockPushGateway implements PushGateway {

    @Override
    public void send(PushPayload payload) {
        System.out.println("========== MOCK PUSH ==========");
        System.out.println("Device : " + payload.deviceToken());
        System.out.println("Title  : " + payload.title());
        System.out.println("Body   : " + payload.body());
        System.out.println("==============================");
    }
}
