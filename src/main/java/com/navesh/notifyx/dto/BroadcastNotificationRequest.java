package com.navesh.notifyx.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Broadcast notification request")
public record BroadcastNotificationRequest(

        @NotBlank(message = "Recipient cannot be blank")
        @Schema(
                description = "Recipient or target audience for the broadcast notification",
                example = "user@example.com"
        )
        String recipient,

        @NotBlank(message = "Message cannot be blank")
        @Schema(
                description = "Message to broadcast",
                example = "Scheduled maintenance will begin at 10:00 PM."
        )
        String message
) {
}