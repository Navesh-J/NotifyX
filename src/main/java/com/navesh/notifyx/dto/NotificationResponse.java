package com.navesh.notifyx.dto;

public record NotificationResponse(
        boolean success,
        String message,
        String provider) {
}
