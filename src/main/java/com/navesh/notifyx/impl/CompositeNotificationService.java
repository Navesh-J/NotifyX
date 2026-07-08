package com.navesh.notifyx.impl;

import com.navesh.notifyx.core.NotificationService;
import com.navesh.notifyx.dto.BroadcastNotificationRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompositeNotificationService {

    private final List<NotificationService> notificationServices;

    public CompositeNotificationService(List<NotificationService> notificationServices) {
        this.notificationServices = notificationServices;
    }

    public void sendToAll(BroadcastNotificationRequest request){
        notificationServices.forEach(service -> service.sendNotification(request));
    }
}