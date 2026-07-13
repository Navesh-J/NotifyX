package com.navesh.notifyx.service;

import com.navesh.notifyx.audit.AuditRepository;
import com.navesh.notifyx.audit.NotificationAuditLog;
import com.navesh.notifyx.config.RetryProperties;
import com.navesh.notifyx.core.AuditStatus;
import com.navesh.notifyx.core.NotificationService;
import com.navesh.notifyx.dto.NotificationRequest;
import com.navesh.notifyx.exception.NotificationDeliveryException;
import com.navesh.notifyx.factory.NotificationServiceFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationRetryService {

    private final RetryProperties retryProperties;
    private final NotificationServiceFactory notificationServiceFactory;
    private final AuditRepository auditRepository;

    @Transactional
    public void retry(NotificationAuditLog auditLog) {
        NotificationRequest request = new NotificationRequest(
                auditLog.getChannel(),
                auditLog.getRecipient(),
                auditLog.getMessage()
        );

        NotificationService service =
                notificationServiceFactory.getService(auditLog.getChannel());

        try {
            log.info(
                    "Retrying notification [{}] via {} (Attempt {})",
                    auditLog.getId(),
                    auditLog.getProvider(),
                    auditLog.getRetryCount() + 1
            );
            service.sendNotification(request);

            auditLog.setAuditStatus(AuditStatus.SUCCESS);
            auditLog.setErrorMessage(null);
            log.info(
                    "Retry successful for notification [{}]",
                    auditLog.getId()
            );
        } catch (NotificationDeliveryException ex) {
            auditLog.setRetryCount(auditLog.getRetryCount() + 1);
            auditLog.setErrorMessage(ex.getMessage());

            if (auditLog.getRetryCount() >= retryProperties.getMaxAttempts()) {
                log.error(
                        "Retry limit exceeded for notification [{}]",
                        auditLog.getId()
                );
                auditLog.setAuditStatus(AuditStatus.RETRY_EXHAUSTED);
            }
            throw ex;
        } finally {
            auditRepository.save(auditLog);
        }
    }
}
