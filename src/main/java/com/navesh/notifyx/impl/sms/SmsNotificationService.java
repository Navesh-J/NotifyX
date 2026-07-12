package com.navesh.notifyx.impl.sms;

import com.navesh.notifyx.core.NotificationChannel;
import com.navesh.notifyx.core.NotificationService;
import com.navesh.notifyx.dto.BroadcastNotificationRequest;
import com.navesh.notifyx.exception.NotificationDeliveryException;
import com.navesh.notifyx.gateway.SmsGateway;
import com.navesh.notifyx.model.SmsPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.navesh.notifyx.dto.NotificationRequest;
import com.navesh.notifyx.dto.NotificationResponse;

@Service
@RequiredArgsConstructor
public class SmsNotificationService implements NotificationService {

    private final SmsGateway smsGateway;

    private SmsPayload buildPayload(NotificationRequest request) {

        return new SmsPayload(
                request.recipient(),
                request.message()
        );
    }

    private SmsPayload buildPayload(BroadcastNotificationRequest request) {

        return new SmsPayload(
                request.recipient(),
                request.message()
        );
    }

    @Override
    public NotificationResponse sendNotification(NotificationRequest request) {

        try {
            SmsPayload payload = buildPayload(request);

            smsGateway.send(payload);

            return new NotificationResponse(
                    true,
                    "SMS Sent Successfully",
                    getProviderName()
            );
        } catch (Exception ex) {
            throw new NotificationDeliveryException(
                    "Unable to send SMS.",
                    ex
            );
        }
    }

    @Override
    public NotificationResponse sendNotification(BroadcastNotificationRequest request) {
        try {
            SmsPayload payload = buildPayload(request);

            smsGateway.send(payload);

            return new NotificationResponse(
                    true,
                    "Broadcast SMS Sent Successfully",
                    getProviderName()
            );
        } catch (Exception ex) {
            throw new NotificationDeliveryException(
                    "Unable to send Broadcast SMS.",
                    ex
            );
        }
    }

    @Override
    public boolean supports(NotificationChannel channel) {
        return channel == NotificationChannel.SMS;
    }

    @Override
    public NotificationChannel getSupportedChannel() {
        return NotificationChannel.SMS;
    }

    @Override
    public String getProviderName() {
        return "Mock SMS gateway";
    }
}
