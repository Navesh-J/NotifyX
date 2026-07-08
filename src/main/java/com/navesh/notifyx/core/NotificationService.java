package com.navesh.notifyx.core;

import com.navesh.notifyx.dto.BroadcastNotificationRequest;
import com.navesh.notifyx.dto.NotificationRequest;
import com.navesh.notifyx.dto.NotificationResponse;

public interface NotificationService {
    NotificationResponse sendNotification(NotificationRequest request);
    NotificationResponse sendNotification(BroadcastNotificationRequest request);
    boolean supports(NotificationChannel channel);
    String getProviderName();
}
