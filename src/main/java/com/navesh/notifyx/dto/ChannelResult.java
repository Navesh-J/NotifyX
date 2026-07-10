package com.navesh.notifyx.dto;

import com.navesh.notifyx.core.NotificationChannel;

public record ChannelResult(
        NotificationChannel notificationChannel,
        String provider,
        boolean success,
        String message
) {}