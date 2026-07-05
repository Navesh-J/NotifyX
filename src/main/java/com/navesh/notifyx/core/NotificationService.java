package com.navesh.notifyx.core;

import com.navesh.notifyx.dto.NotificationRequest;
import com.navesh.notifyx.dto.NotificationResponse;

public interface NotificationService {
    NotificationResponse send(NotificationRequest request);
    NotificationChannel getChannel();
}
