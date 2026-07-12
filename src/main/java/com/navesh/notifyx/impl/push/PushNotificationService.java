package com.navesh.notifyx.impl.push;

import com.navesh.notifyx.core.NotificationChannel;
import com.navesh.notifyx.core.NotificationService;
import com.navesh.notifyx.dto.BroadcastNotificationRequest;
import com.navesh.notifyx.dto.NotificationRequest;
import com.navesh.notifyx.dto.NotificationResponse;
import com.navesh.notifyx.exception.NotificationDeliveryException;
import com.navesh.notifyx.gateway.PushGateway;
import com.navesh.notifyx.model.PushPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PushNotificationService implements NotificationService {

    private final PushGateway pushGateway;

    @Override
    public NotificationResponse sendNotification(NotificationRequest request) {
        try {
            PushPayload payload = buildPayload(request);

            pushGateway.send(payload);

            return new NotificationResponse(
                    true,
                    "Push Notification sent successfully",
                    getProviderName()
            );
        } catch (Exception ex) {
            throw new NotificationDeliveryException(
                    "Unable to send push notification. ",
                    ex
            );
        }
    }

    @Override
    public NotificationResponse sendNotification(BroadcastNotificationRequest request) {
        try {
            PushPayload payload = buildPayload(request);

            pushGateway.send(payload);

            return new NotificationResponse(
                    true,
                    "Broadcast Push sent successfully",
                    getProviderName()
            );
        } catch (Exception ex) {
            throw new NotificationDeliveryException(
                    "Unable to send push notification. ",
                    ex
            );
        }
    }

    @Override
    public boolean supports(NotificationChannel channel) {
        return channel == NotificationChannel.PUSH;
    }

    @Override
    public NotificationChannel getSupportedChannel() {
        return NotificationChannel.PUSH;
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

    private PushPayload buildPayload(BroadcastNotificationRequest request) {
        return new PushPayload(
                request.recipient(),
                "NotifyX",
                request.message()
        );
    }
}
