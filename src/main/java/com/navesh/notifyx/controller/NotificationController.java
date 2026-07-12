package com.navesh.notifyx.controller;

import com.navesh.notifyx.dto.BroadcastNotificationRequest;
import com.navesh.notifyx.dto.BroadcastNotificationResponse;
import com.navesh.notifyx.dto.NotificationRequest;
import com.navesh.notifyx.dto.NotificationResponse;
import com.navesh.notifyx.service.NotificationApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationApplicationService notificationApplicationService;

    @PostMapping("/send")
    public NotificationResponse sendNotification(
            @Valid @RequestBody NotificationRequest request) {
        return notificationApplicationService.sendNotification(request);
    }

    @PostMapping("/send-all")
    public BroadcastNotificationResponse sendAll(
            @Valid @RequestBody BroadcastNotificationRequest request){

        return notificationApplicationService.sendNotificationToAll(request);
    }
}
