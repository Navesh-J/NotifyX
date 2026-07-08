package com.navesh.notifyx.impl;

import com.navesh.notifyx.core.NotificationChannel;
import com.navesh.notifyx.core.NotificationService;
import com.navesh.notifyx.dto.BroadcastNotificationRequest;
import com.navesh.notifyx.gateway.SmsGateway;
import com.navesh.notifyx.model.SmsPayload;
import org.springframework.stereotype.Service;

import com.navesh.notifyx.dto.NotificationRequest;
import com.navesh.notifyx.dto.NotificationResponse;

@Service
//@Profile("sms")
public class SmsNotificationService implements NotificationService {

    private final SmsGateway smsGateway;

    public SmsNotificationService(SmsGateway smsGateway) {
        this.smsGateway = smsGateway;
    }

    private SmsPayload buildPayload(NotificationRequest request) {
        return new SmsPayload(
                request.recipient(),
                request.message()
        );
    }

    @Override
    public NotificationResponse sendNotification(NotificationRequest request){

        SmsPayload payload = buildPayload(request);

        smsGateway.send(payload);

        return new NotificationResponse(
            true,
            "SMS Sent Successfully",
            getProviderName()
        );
        
    }

    @Override
    public NotificationResponse sendNotification(BroadcastNotificationRequest request) {
        SmsPayload payload = new SmsPayload(
                request.recipient(),
                request.message()
        );

        smsGateway.send(payload);

        return new NotificationResponse(
                true,
                "Broadcast SMS Sent Successfully",
                getProviderName()
        );
    }

    @Override
    public boolean supports(NotificationChannel channel) {
        return channel == NotificationChannel.SMS;
    }

    @Override
    public String getProviderName() {
        return "Mock SMS gateway";
    }
}
