package com.navesh.notifyx.service;

import com.navesh.notifyx.audit.AuditRepository;
import com.navesh.notifyx.audit.NotificationAuditLog;
import com.navesh.notifyx.core.NotificationService;
import com.navesh.notifyx.core.NotificationStatus;
import com.navesh.notifyx.dto.BroadcastNotificationRequest;
import com.navesh.notifyx.dto.BroadcastNotificationResponse;
import com.navesh.notifyx.dto.NotificationRequest;
import com.navesh.notifyx.dto.NotificationResponse;
import com.navesh.notifyx.factory.NotificationServiceFactory;
import com.navesh.notifyx.impl.CompositeNotificationService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class NotificationApplicationService {

    private final NotificationServiceFactory notificationServiceFactory;
    private final CompositeNotificationService compositeNotificationService;
    private final AuditRepository auditRepository;

    public NotificationApplicationService(
            NotificationServiceFactory notificationServiceFactory,
            CompositeNotificationService compositeNotificationService,
            AuditRepository auditRepository) {
        this.notificationServiceFactory = notificationServiceFactory;
        this.compositeNotificationService = compositeNotificationService;
        this.auditRepository = auditRepository;
    }

    public NotificationResponse sendNotification(NotificationRequest request){
        NotificationService service =
                notificationServiceFactory.getService(request.channel());

        try {
            NotificationResponse response = service.sendNotification(request);

            saveAudit(
                    request,
                    service.getProviderName(),
                    NotificationStatus.SUCCESS,
                    null
            );
            return response;
        } catch (Exception ex) {
            saveAudit(
                    request,
                    service.getProviderName(),
                    NotificationStatus.FAILED,
                    ex.getMessage()
            );
            throw ex;
        }
    }

    public BroadcastNotificationResponse sendNotificationToAll(BroadcastNotificationRequest request){
        return compositeNotificationService.sendToAll(request);
    }

    private void saveAudit(
            NotificationRequest request,
            String provider,
            NotificationStatus status,
            String errorMessage
    ) {
        NotificationAuditLog auditLog = new NotificationAuditLog();
        auditLog.setChannel(request.channel());
        auditLog.setProvider(provider);
        auditLog.setRecipient(request.recipient());
        auditLog.setMessage(request.message());
        auditLog.setStatus(status);
        auditLog.setErrorMessage(errorMessage);
        auditLog.setSentAt(LocalDateTime.now());

        auditRepository.save(auditLog);
    }
}
