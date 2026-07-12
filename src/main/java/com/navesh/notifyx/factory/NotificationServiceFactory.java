package com.navesh.notifyx.factory;

import com.navesh.notifyx.core.NotificationChannel;
import com.navesh.notifyx.core.NotificationService;
import com.navesh.notifyx.exception.ChannelUnavailableException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class NotificationServiceFactory {

    private final List<NotificationService> services;

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
