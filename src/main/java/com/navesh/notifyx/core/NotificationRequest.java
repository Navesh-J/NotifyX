package com.navesh.notifyx.core;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record NotificationRequest (

        @NotNull(message = "Channel is required")
        NotificationChannel channel,

        @NotBlank(message = "Recipient cannot be blank")
        String recipient,

        @NotBlank(message = "Message cannot be blank")
        String message
) {}