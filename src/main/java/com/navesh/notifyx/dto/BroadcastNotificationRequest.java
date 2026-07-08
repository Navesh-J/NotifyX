package com.navesh.notifyx.dto;

import jakarta.validation.constraints.NotBlank;

public record BroadcastNotificationRequest(

        @NotBlank(message = "Recipient cannot be blank")
        String recipient,

        @NotBlank(message = "Message cannot be blank")
        String message
) {
}
