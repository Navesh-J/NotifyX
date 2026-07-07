package com.navesh.notifyx.model;

public record PushPayload(
        String deviceToken,
        String title,
        String body
) {
}
