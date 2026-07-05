package com.navesh.notifyx.service;

import com.navesh.notifyx.dto.NotificationRequest;
import com.navesh.notifyx.dto.NotificationResponse;
import com.navesh.notifyx.factory.NotificationServiceFactory;
import org.springframework.stereotype.Service;

@Service
public class NotificationApplicationService {

    private final NotificationServiceFactory notificationServiceFactory;

    public NotificationApplicationService(NotificationServiceFactory notificationServiceFactory) {
        this.notificationServiceFactory = notificationServiceFactory;
    }

    public NotificationResponse send(NotificationRequest request){
        return notificationServiceFactory
                .getService(request.channel())
                .sendNotification(request);
    }
}
