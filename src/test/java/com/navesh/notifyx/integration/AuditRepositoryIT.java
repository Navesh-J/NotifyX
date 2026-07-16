package com.navesh.notifyx.integration;

import com.navesh.notifyx.audit.AuditRepository;
import com.navesh.notifyx.audit.NotificationAuditLog;
import com.navesh.notifyx.core.AuditStatus;
import com.navesh.notifyx.core.NotificationChannel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class AuditRepositoryIT {

    @Autowired
    private AuditRepository repository;

    @Test
    void shouldFindPendingNotifications() {

        NotificationAuditLog log =
                NotificationAuditLog.builder()
                        .channel(NotificationChannel.EMAIL)
                        .recipient("john@example.com")
                        .message("Hello")
                        .provider("Email")
                        .auditStatus(AuditStatus.RETRY_PENDING)
                        .retryCount(1)
                        .build();

        repository.save(log);

        List<NotificationAuditLog> result =
                repository.findByAuditStatusAndRetryCountLessThan(
                        AuditStatus.RETRY_PENDING,
                        3
                );

        assertEquals(1, result.size());
    }
}