package com.navesh.notifyx.service;

import com.navesh.notifyx.audit.AuditService;
import com.navesh.notifyx.core.NotificationService;
import com.navesh.notifyx.core.NotificationStatus;
import com.navesh.notifyx.dto.BroadcastNotificationRequest;
import com.navesh.notifyx.dto.BroadcastNotificationResponse;
import com.navesh.notifyx.dto.NotificationRequest;
import com.navesh.notifyx.dto.NotificationResponse;
import com.navesh.notifyx.factory.NotificationServiceFactory;
import com.navesh.notifyx.impl.CompositeNotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class NotificationApplicationService {

    private final NotificationServiceFactory notificationServiceFactory;
    private final CompositeNotificationService compositeNotificationService;
    private final AuditService auditService;
    private static final Logger log =
            LoggerFactory.getLogger(NotificationApplicationService.class);

    public NotificationApplicationService(
            NotificationServiceFactory notificationServiceFactory,
            CompositeNotificationService compositeNotificationService,
            AuditService auditService) {
        this.notificationServiceFactory = notificationServiceFactory;
        this.compositeNotificationService = compositeNotificationService;
        this.auditService = auditService;
    }

    public NotificationResponse sendNotification(NotificationRequest request){
        NotificationService service =
                notificationServiceFactory.getService(request.channel());

        try {
            log.info(
                    "Received {} notification request for {}",
                    request.channel(),
                    request.recipient()
            );

            NotificationResponse response = service.sendNotification(request);

            auditService.audit(
                    request,
                    service.getProviderName(),
                    NotificationStatus.SUCCESS,
                    null
            );

            log.info(
                    "{} notification completed successfully.",
                    request.channel()
            );

            return response;
        } catch (Exception ex) {
            auditService.audit(
                    request,
                    service.getProviderName(),
                    NotificationStatus.FAILED,
                    ex.getMessage()
            );

            log.error(
                    "{} notification failed.",
                    request.channel(),
                    ex
            );

            throw ex;
        }
    }

    public BroadcastNotificationResponse sendNotificationToAll(
            BroadcastNotificationRequest request){

        log.info(
                "Broadcast notification requested for {}",
                request.recipient()
        );

        BroadcastNotificationResponse response =
                compositeNotificationService.sendToAll(request);

        response.results()
                .forEach(result -> auditService.audit(request, result));

        log.info(
                "Broadcast completed. Success: {}, Failed: {}",
                response.successfulChannels(),
                response.failedChannels()
        );

        return response;
    }

}
