package com.navesh.notifyx.integration;

import com.navesh.notifyx.audit.AuditRepository;
import com.navesh.notifyx.audit.NotificationAuditLog;
import com.navesh.notifyx.core.AuditStatus;
import com.navesh.notifyx.core.NotificationChannel;
import com.navesh.notifyx.scheduler.NotificationScheduler;
import com.navesh.notifyx.service.NotificationRetryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.mockito.Mockito.verify;

@SpringBootTest
@ActiveProfiles("test")
class RetrySchedulerIT {

    @Autowired
    private NotificationScheduler scheduler;

    @Autowired
    private AuditRepository repository;

    @MockitoBean
    private NotificationRetryService retryService;

    @Test
    void shouldRetryPendingNotifications() {

        NotificationAuditLog audit =
                NotificationAuditLog.builder()
                        .channel(NotificationChannel.EMAIL)
                        .recipient("john@example.com")
                        .message("Hello")
                        .provider("Email")
                        .auditStatus(AuditStatus.RETRY_PENDING)
                        .retryCount(0)
                        .build();

        repository.save(audit);

        scheduler.retryFailedNotifications();

        verify(retryService)
                .retry(audit);
    }
}