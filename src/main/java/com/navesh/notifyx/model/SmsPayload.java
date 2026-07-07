package com.navesh.notifyx.model;

public record SmsPayload(
        String phoneNumber,
        String message
) {
}
