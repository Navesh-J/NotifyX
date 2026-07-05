package com.navesh.notifyx.impl;

import com.navesh.notifyx.config.ProviderProperties;
import com.navesh.notifyx.core.NotificationChannel;
import com.navesh.notifyx.dto.NotificationRequest;
import com.navesh.notifyx.dto.NotificationResponse;
import com.navesh.notifyx.core.NotificationService;
import org.springframework.stereotype.Service;

@Service
//@Profile("email")
public class EmailNotificationService implements NotificationService {

    private ProviderProperties providerProperties;

    public EmailNotificationService(ProviderProperties providerProperties) {
        this.providerProperties = providerProperties;
    }

    @Override
    public NotificationChannel getChannel() {
        return NotificationChannel.EMAIL;
    }

    @Override
    public NotificationResponse send(NotificationRequest request) {
        System.out.println(providerProperties.getName());
        System.out.println("Sending Email to: " + request.recipient());

        return new NotificationResponse(
                true,
                "Email sent successfully",
                providerProperties.getName()
        );
    }
}