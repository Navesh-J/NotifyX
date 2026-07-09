package com.navesh.notifyx.service;

import com.navesh.notifyx.dto.BroadcastNotificationRequest;
import com.navesh.notifyx.dto.BroadcastNotificationResponse;
import com.navesh.notifyx.dto.NotificationRequest;
import com.navesh.notifyx.dto.NotificationResponse;
import com.navesh.notifyx.factory.NotificationServiceFactory;
import com.navesh.notifyx.impl.CompositeNotificationService;
import org.springframework.stereotype.Service;

@Service
public class NotificationApplicationService {

    private final NotificationServiceFactory notificationServiceFactory;
    private final CompositeNotificationService compositeNotificationService;

    public NotificationApplicationService(
            NotificationServiceFactory notificationServiceFactory,
            CompositeNotificationService compositeNotificationService) {
        this.notificationServiceFactory = notificationServiceFactory;
        this.compositeNotificationService = compositeNotificationService;
    }

    public NotificationResponse sendNotification(NotificationRequest request){
        return notificationServiceFactory
                .getService(request.channel())
                .sendNotification(request);
    }

    public BroadcastNotificationResponse sendNotificationToAll(BroadcastNotificationRequest request){
        return compositeNotificationService.sendToAll(request);
    }
}
