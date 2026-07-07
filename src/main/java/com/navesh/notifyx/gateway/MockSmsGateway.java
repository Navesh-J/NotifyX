package com.navesh.notifyx.gateway;

import com.navesh.notifyx.model.SmsPayload;
import org.springframework.stereotype.Component;

@Component
public class MockSmsGateway implements SmsGateway {

    @Override
    public void send(SmsPayload payload) {
        System.out.println("========== MOCK SMS ==========");
        System.out.println("To      : " + payload.phoneNumber());
        System.out.println("Message : " + payload.message());
        System.out.println("==============================");
    }
}
