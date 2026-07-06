package com.navesh.notifyx.impl;

import com.navesh.notifyx.core.NotificationChannel;
import com.navesh.notifyx.core.NotificationService;
import org.springframework.stereotype.Service;

import com.navesh.notifyx.dto.NotificationRequest;
import com.navesh.notifyx.dto.NotificationResponse;

@Service
//@Profile("sms")
public class SmsNotificationService implements NotificationService {
    
    @Override
    public NotificationResponse sendNotification(NotificationRequest request){

        System.out.println("Sending SMS to: "+request.recipient());

        return new NotificationResponse(
            true,
            "SMS Sent Successfully",
            "SMS"
        );
        
    }

    @Override
    public boolean supports(NotificationChannel channel) {
        return channel == NotificationChannel.SMS;
    }

    @Override
    public String getProviderName() {
        return "SMS Notification Service";
    }
}
