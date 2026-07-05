package com.navesh.notifyx.dto;

import com.navesh.notifyx.core.NotificationChannel;
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