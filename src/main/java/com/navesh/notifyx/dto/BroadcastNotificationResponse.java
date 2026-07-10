package com.navesh.notifyx.dto;

import java.time.LocalDateTime;
import java.util.List;

public record BroadcastNotificationResponse(
        int totalChannels,
        int successfulChannels,
        int failedChannels,

        List<ChannelResult> results,

        LocalDateTime timestamp
) {
}
