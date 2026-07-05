package com.navesh.notifyx.factory;

import com.navesh.notifyx.core.NotificationChannel;
import com.navesh.notifyx.core.NotificationService;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
public class NotificationServiceFactory {

    private final Map<NotificationChannel, NotificationService> services = new EnumMap<>(NotificationChannel.class);

    public NotificationServiceFactory(List<NotificationService> notificationServices) {

        for(NotificationService notificationService : notificationServices){
            services.put(notificationService.getChannel(), notificationService);
        }
    }

    public NotificationService getService(NotificationChannel channel){
        return services.get(channel);
    }
}
