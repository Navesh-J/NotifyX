package com.navesh.notifyx.impl;

import com.navesh.notifyx.core.NotificationChannel;
import com.navesh.notifyx.core.NotificationService;
import com.navesh.notifyx.dto.BroadcastNotificationRequest;
import com.navesh.notifyx.exception.NotificationDeliveryException;
import com.navesh.notifyx.gateway.SmsGateway;
import com.navesh.notifyx.model.SmsPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.navesh.notifyx.dto.NotificationRequest;
import com.navesh.notifyx.dto.NotificationResponse;

@Service
public class SmsNotificationService implements NotificationService {

    private final SmsGateway smsGateway;
    private static final Logger log =
            LoggerFactory.getLogger(SmsNotificationService.class);

    public SmsNotificationService(SmsGateway smsGateway) {
        this.smsGateway = smsGateway;
    }

    private SmsPayload buildPayload(NotificationRequest request) {
        log.info("Sending SMS to {}", request.recipient());

        return new SmsPayload(
                request.recipient(),
                request.message()
        );
    }

    private SmsPayload buildPayload(BroadcastNotificationRequest request) {
        log.info("Sending Broadcast SMS to {}", request.recipient());

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

            log.info("SMS successfully sent to {}", request.recipient());

            return new NotificationResponse(
                    true,
                    "SMS Sent Successfully",
                    getProviderName()
            );
        } catch (Exception ex) {
            log.error("Failed to send SMS", ex);
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

            log.info("Broadcast SMS successfully sent to {}", request.recipient());

            return new NotificationResponse(
                    true,
                    "Broadcast SMS Sent Successfully",
                    getProviderName()
            );
        } catch (Exception ex) {
            log.error("Failed to send Broadcast SMS", ex);
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
