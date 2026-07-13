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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class NotificationApplicationService {

    private final NotificationServiceFactory notificationServiceFactory;
    private final CompositeNotificationService compositeNotificationService;
    private final AuditService auditService;

    public NotificationResponse sendNotification(NotificationRequest request){
        NotificationService service =
                notificationServiceFactory.getService(request.channel());

        try {
            NotificationResponse response = service.sendNotification(request);

            auditService.audit(
                    request,
                    service.getProviderName(),
                    NotificationStatus.SUCCESS,
                    null
            );

            return response;
        } catch (Exception ex) {
            auditService.audit(
                    request,
                    service.getProviderName(),
                    NotificationStatus.FAILED,
                    ex.getMessage()
            );

            throw ex;
        }
    }

    public BroadcastNotificationResponse sendNotificationToAll(
            BroadcastNotificationRequest request){

        BroadcastNotificationResponse response =
                compositeNotificationService.sendToAll(request);

        response.results()
                .forEach(result -> auditService.audit(request, result));

        return response;
    }

}
