package com.navesh.notifyx.dto;

import com.navesh.notifyx.core.NotificationChannel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Notification request")
public record NotificationRequest (

        @NotNull(message = "Channel is required")
        @Schema(
                description = "Notification Channel",
                example = "EMAIL"
        )
        NotificationChannel channel,

        @NotBlank(message = "Recipient cannot be blank")
        @Schema(
                description = "Recipient of the notification",
                example = "user@example.com"
        )
        String recipient,

        @NotBlank(message = "Message cannot be blank")
        @Schema(
                description = "Notification message",
                example = "Welcome to NotifyX!"
        )
        String message
) {}