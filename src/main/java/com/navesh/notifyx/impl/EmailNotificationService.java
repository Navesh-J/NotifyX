package com.navesh.notifyx.impl;

import com.navesh.notifyx.config.ProviderProperties;
import com.navesh.notifyx.core.NotificationRequest;
import com.navesh.notifyx.core.NotificationResponse;
import com.navesh.notifyx.core.NotificationService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("email")
public class EmailNotificationService implements NotificationService {

    private ProviderProperties providerProperties;

    public EmailNotificationService(ProviderProperties providerProperties) {
        this.providerProperties = providerProperties;
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