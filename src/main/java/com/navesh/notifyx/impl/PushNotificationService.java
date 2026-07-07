package com.navesh.notifyx.impl;

import com.navesh.notifyx.core.NotificationChannel;
import com.navesh.notifyx.core.NotificationService;
import com.navesh.notifyx.dto.NotificationRequest;
import com.navesh.notifyx.dto.NotificationResponse;
import com.navesh.notifyx.gateway.PushGateway;
import com.navesh.notifyx.model.PushPayload;
import org.springframework.stereotype.Service;

@Service
public class PushNotificationService implements NotificationService {

    private final PushGateway pushGateway;

    public PushNotificationService(PushGateway pushGateway) {
        this.pushGateway = pushGateway;
    }

    @Override
    public NotificationResponse sendNotification(NotificationRequest request){
        PushPayload payload = buildPayload(request);

        pushGateway.send(payload);

        return new NotificationResponse(
                true,
                "Push Notification sent successfully",
                getProviderName()
        );
    }

    @Override
    public boolean supports(NotificationChannel channel) {
        return channel == NotificationChannel.PUSH;
    }

    @Override
    public String getProviderName() {
        return "Mock Push Gateway";
    }

    private PushPayload buildPayload(NotificationRequest request) {
        return new PushPayload(
                request.recipient(),
                "NotifyX",
                request.message()
        );
    }
}
