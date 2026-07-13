package com.navesh.notifyx.exception;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Standard API Error")
public record ErrorResponse(
        @Schema(example = "2026-07-12T11:15:22")
        LocalDateTime timestamp,

        @Schema(example = "500")
        int status,

        @Schema(example = "Notification Delivery Failed")
        String error,

        @Schema(example = "Unable to send email.")
        String message,

        @Schema(example = "/api/notifications/send")
        String path
) {
}
