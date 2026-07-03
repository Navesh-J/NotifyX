package com.navesh.notifyx.core;

public record NotificationResponse(
        boolean success,
        String message,
        String provider) {
}
