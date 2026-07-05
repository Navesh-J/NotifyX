package com.navesh.notifyx.impl;

import com.navesh.notifyx.core.NotificationChannel;
import com.navesh.notifyx.core.NotificationService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.navesh.notifyx.core.NotificationRequest;
import com.navesh.notifyx.core.NotificationResponse;

@Service
//@Profile("sms")
public class SmsNotificationService implements NotificationService {
    
    @Override
    public NotificationResponse send(NotificationRequest request){

        System.out.println("Sending SMS to: "+request.recipient());

        return new NotificationResponse(
            true,
            "SMS Sent Successfully",
            "SMS"
        );
        
    }

    @Override
    public NotificationChannel getChannel(){
        return NotificationChannel.SMS;
    }
}
