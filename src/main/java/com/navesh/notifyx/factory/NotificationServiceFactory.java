package com.navesh.notifyx.factory;

import com.navesh.notifyx.core.NotificationChannel;
import com.navesh.notifyx.core.NotificationService;
import com.navesh.notifyx.exception.NotificationServiceNotFoundException;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class NotificationServiceFactory {

    private final Map<NotificationChannel, NotificationService> services = new EnumMap<>(NotificationChannel.class);

    public NotificationServiceFactory(List<NotificationService> notificationServices) {

        for(NotificationService notificationService : notificationServices){
            services.put(notificationService.getChannel(), notificationService);
        }
    }

    public NotificationService getService(NotificationChannel channel){
        return Optional.ofNullable(services.get(channel))
                .orElseThrow(() ->
                        new NotificationServiceNotFoundException(
                                "Unsupported notification channel " + channel
                        )
                );
    }
}
