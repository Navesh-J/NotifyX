package com.navesh.notifyx.scheduler;

import com.navesh.notifyx.audit.AuditRepository;
import com.navesh.notifyx.audit.NotificationAuditLog;
import com.navesh.notifyx.config.RetryProperties;
import com.navesh.notifyx.core.AuditStatus;
import com.navesh.notifyx.service.NotificationRetryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationScheduler {

    private final RetryProperties retryProperties;
    private final AuditRepository auditRepository;
    private final NotificationRetryService retryService;

    @Scheduled(fixedDelayString = "${notification.retry.fixed-delay}")
    public void retryFailedNotifications() {
        log.info("Retry scheduler started.");
        List<NotificationAuditLog> pendingNotifications =
                auditRepository.findByAuditStatusAndRetryCountLessThan(
                        AuditStatus.RETRY_PENDING,
                        retryProperties.getMaxAttempts()
                );

        if (pendingNotifications.isEmpty()) {
            log.info("No notifications pending retry.");
            return;
        }

        log.info(
                "{} notification(s) found for retry.",
                pendingNotifications.size()
        );

        for (NotificationAuditLog auditLog : pendingNotifications) {
            try {
                retryService.retry(auditLog);
            } catch (Exception ex) {
                log.warn(
                        "Retry failed for notification [{}]",
                        auditLog.getId()
                );
            }
        }
        log.info("Retry scheduler completed.");
    }
}
