package com.navesh.notifyx.core;

public record NotificationRequest (
        NotificationChannel channel,
        String recipient,
        String message
) {}