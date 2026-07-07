package com.navesh.notifyx.gateway;

import com.navesh.notifyx.model.SmsPayload;

public interface SmsGateway {
    void send(SmsPayload payload);
}
