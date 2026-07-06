package com.navesh.notifyx.model;

public record EmailPayload (
        String from,
        String to,
        String subject,
        String body
) {
}
