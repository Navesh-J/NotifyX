package com.navesh.notifyx.factory;

import com.navesh.notifyx.core.NotificationChannel;
import com.navesh.notifyx.core.NotificationService;
import com.navesh.notifyx.exception.ChannelUnavailableException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NotificationServiceFactory {

    private final List<NotificationService> services;

    public NotificationServiceFactory(List<NotificationService> services) {
        this.services = services;
    }

    public NotificationService getService(NotificationChannel channel){
        return services.stream()
                .filter(service -> service.supports(channel))
                .findFirst()
                .orElseThrow(() ->
                    new ChannelUnavailableException(
                            "Unsupported notification channel: "+ channel
                ));
    }
}
