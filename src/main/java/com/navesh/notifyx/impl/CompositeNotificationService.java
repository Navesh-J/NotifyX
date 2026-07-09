package com.navesh.notifyx.impl;

import com.navesh.notifyx.core.NotificationService;
import com.navesh.notifyx.dto.BroadcastNotificationRequest;
import com.navesh.notifyx.dto.BroadcastNotificationResponse;
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

    public BroadcastNotificationResponse sendToAll(BroadcastNotificationRequest request){
        List<String> successful = new ArrayList<>();
        List<String> failed = new ArrayList<>();

        for(NotificationService service : notificationServices){
            try{
                service.sendNotification(request);
                successful.add(service.getProviderName());
            }catch (Exception e){
                failed.add(service.getProviderName());
            }
        }

        return new BroadcastNotificationResponse(
                notificationServices.size(),
                successful.size(),
                failed.size(),
                successful,
                failed,
                LocalDateTime.now()
        );
    }
}