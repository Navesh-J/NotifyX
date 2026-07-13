package com.navesh.notifyx.controller;

import com.navesh.notifyx.dto.BroadcastNotificationRequest;
import com.navesh.notifyx.dto.BroadcastNotificationResponse;
import com.navesh.notifyx.dto.NotificationRequest;
import com.navesh.notifyx.dto.NotificationResponse;
import com.navesh.notifyx.service.NotificationApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "Notifications",
        description = "Notification Management APIs"
)
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationApplicationService notificationApplicationService;

    @Operation(
            summary = "Send Notification",
            description = "Sends a notification using the requested channel."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(ref = "#/components/responses/BadRequest"),
            @ApiResponse(ref = "#/components/responses/InternalError")

    })
    @PostMapping("/send")
    public NotificationResponse sendNotification(
            @Valid @RequestBody NotificationRequest request) {
        return notificationApplicationService.sendNotification(request);
    }

    @Operation(
            summary = "Broadcast Notification",
            description = "Sends a notification using all available channels."
    )
    @PostMapping("/send-all")
    public BroadcastNotificationResponse sendAll(
            @Valid @RequestBody BroadcastNotificationRequest request){

        return notificationApplicationService.sendNotificationToAll(request);
    }
}
