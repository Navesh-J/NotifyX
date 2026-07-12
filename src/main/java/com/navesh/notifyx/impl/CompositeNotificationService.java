package com.navesh.notifyx.impl;

import com.navesh.notifyx.core.NotificationService;
import com.navesh.notifyx.dto.BroadcastNotificationRequest;
import com.navesh.notifyx.dto.BroadcastNotificationResponse;
import com.navesh.notifyx.dto.ChannelResult;
import com.navesh.notifyx.dto.NotificationResponse;
import com.navesh.notifyx.exception.NotificationDeliveryException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CompositeNotificationService {

    private final List<NotificationService> notificationServices;

    public CompositeNotificationService(List<NotificationService> notificationServices) {
        this.notificationServices = notificationServices;
    }

    public BroadcastNotificationResponse sendToAll(BroadcastNotificationRequest request) {
        List<ChannelResult> results = new ArrayList<>();
        int successful = 0;
        int failed = 0;

        for (NotificationService service : notificationServices) {
            try {
                NotificationResponse response = service.sendNotification(request);
                results.add(
                        new ChannelResult(
                                service.getSupportedChannel(),
                                service.getProviderName(),
                                true,
                                response.message()
                        )
                );
                successful++;

            } catch (NotificationDeliveryException ex) {
                results.add(new ChannelResult(
                                service.getSupportedChannel(),
                                service.getProviderName(),
                                false,
                                ex.getMessage()
                        )
                );
                failed++;
            }
        }

        return new BroadcastNotificationResponse(
                results.size(),
                successful,
                failed,
                results,
                LocalDateTime.now()
        );
    }
}