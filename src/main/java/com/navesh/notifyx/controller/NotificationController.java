package com.navesh.notifyx.controller;

import com.navesh.notifyx.core.NotificationRequest;
import com.navesh.notifyx.core.NotificationResponse;
import com.navesh.notifyx.factory.NotificationServiceFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationServiceFactory notificationServiceFactory;

    public NotificationController(NotificationServiceFactory notificationServiceFactory) {
        this.notificationServiceFactory = notificationServiceFactory;
    }

    @PostMapping("/send")
    public NotificationResponse sendNotification(@RequestBody NotificationRequest request) {
        return notificationServiceFactory
                .getService(request.channel())
                .send(request);
    }
}
