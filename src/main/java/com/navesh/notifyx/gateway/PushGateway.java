package com.navesh.notifyx.gateway;

import com.navesh.notifyx.model.PushPayload;

public interface PushGateway {
    void send(PushPayload payload);
}
