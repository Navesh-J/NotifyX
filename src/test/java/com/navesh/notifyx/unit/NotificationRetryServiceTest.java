package com.navesh.notifyx.unit;

import com.navesh.notifyx.audit.AuditRepository;
import com.navesh.notifyx.audit.NotificationAuditLog;
import com.navesh.notifyx.config.RetryProperties;
import com.navesh.notifyx.core.AuditStatus;
import com.navesh.notifyx.core.NotificationChannel;
import com.navesh.notifyx.core.NotificationService;
import com.navesh.notifyx.dto.BroadcastNotificationRequest;
import com.navesh.notifyx.dto.NotificationRequest;
import com.navesh.notifyx.dto.NotificationResponse;
import com.navesh.notifyx.exception.NotificationDeliveryException;
import com.navesh.notifyx.factory.NotificationServiceFactory;
import com.navesh.notifyx.service.NotificationRetryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class NotificationRetryServiceTest {

    @Mock
    private NotificationServiceFactory notificationServiceFactory;

    @Mock
    private NotificationService notificationService;

    @Mock
    private AuditRepository auditRepository;

    @Mock
    private RetryProperties retryProperties;

    @InjectMocks
    private NotificationRetryService retryService;

    @Test
    void shouldRetrySuccessfully() {

        NotificationAuditLog audit = new NotificationAuditLog();

        audit.setId(1L);
        audit.setChannel(NotificationChannel.EMAIL);
        audit.setRecipient("john@example.com");
        audit.setMessage("Hello");
        audit.setProvider("Email");
        audit.setAuditStatus(AuditStatus.RETRY_PENDING);
        audit.setRetryCount(0);

        when(notificationServiceFactory.getService(NotificationChannel.EMAIL))
                .thenReturn(notificationService);

        when(notificationService.sendNotification(any(NotificationRequest.class)))
                .thenReturn(
                        new NotificationResponse(
                                true,
                                "Success",
                                "Email"
                        )
                );

        when(notificationService.sendNotification(any(BroadcastNotificationRequest.class)))
                .thenReturn(
                        new NotificationResponse(
                                true,
                                "Success",
                                "Email"
                        )
                );

        retryService.retry(audit);

        assertEquals(
                AuditStatus.SUCCESS,
                audit.getAuditStatus()
        );

        assertNull(
                audit.getErrorMessage()
        );

        verify(auditRepository).save(audit);
    }

    @Test
    void shouldIncrementRetryCountWhenRetryFails() {

        NotificationAuditLog audit = new NotificationAuditLog();

        audit.setId(1L);
        audit.setChannel(NotificationChannel.EMAIL);
        audit.setRecipient("john@example.com");
        audit.setMessage("Hello");
        audit.setProvider("Email");
        audit.setAuditStatus(AuditStatus.RETRY_PENDING);
        audit.setRetryCount(0);

        when(notificationServiceFactory.getService(NotificationChannel.EMAIL))
                .thenReturn(notificationService);

        when(retryProperties.getMaxAttempts())
                .thenReturn(3);

        when(notificationService.sendNotification(any(NotificationRequest.class)))
                .thenThrow(
                        new NotificationDeliveryException(
                                "SMTP Down"
                        )
                );

        when(notificationService
                .sendNotification(any(BroadcastNotificationRequest.class)))
                .thenThrow(
                        new NotificationDeliveryException(
                                "SMTP Down"
                        )
                );

        assertThrows(
                NotificationDeliveryException.class,
                () -> retryService.retry(audit)
        );

        assertEquals(
                1,
                audit.getRetryCount()
        );

        assertEquals(
                AuditStatus.RETRY_PENDING,
                audit.getAuditStatus()
        );

        verify(auditRepository).save(audit);
    }

    @Test
    void shouldKeepRetryPendingWhenBelowMaxAttempts() {

        NotificationAuditLog audit = new NotificationAuditLog();

        audit.setChannel(NotificationChannel.EMAIL);
        audit.setRecipient("john@example.com");
        audit.setMessage("Hello");
        audit.setProvider("Email");
        audit.setRetryCount(1);
        audit.setAuditStatus(AuditStatus.RETRY_PENDING);

        when(notificationServiceFactory.getService(NotificationChannel.EMAIL))
                .thenReturn(notificationService);

        when(retryProperties.getMaxAttempts())
                .thenReturn(3);

        when(notificationService.sendNotification(any(NotificationRequest.class)))
                .thenThrow(
                        new NotificationDeliveryException(
                                "SMTP Down"
                        )
                );

        when(notificationService
                .sendNotification(any(BroadcastNotificationRequest.class)))
                .thenThrow(
                        new NotificationDeliveryException(
                                "SMTP Down"
                        )
                );

        assertThrows(
                NotificationDeliveryException.class,
                () -> retryService.retry(audit)
        );

        assertEquals(
                2,
                audit.getRetryCount()
        );

        assertEquals(
                AuditStatus.RETRY_PENDING,
                audit.getAuditStatus()
        );
    }

    @Test
    void shouldUseCorrectNotificationService() {

        NotificationAuditLog audit = new NotificationAuditLog();

        audit.setChannel(NotificationChannel.EMAIL);

        when(notificationServiceFactory.getService(NotificationChannel.EMAIL))
                .thenReturn(notificationService);

        when(notificationService.sendNotification(any(NotificationRequest.class)))
                .thenReturn(
                        new NotificationResponse(
                                true,
                                "Success",
                                "Email"
                        )
                );

        when(notificationService.
                sendNotification(any(BroadcastNotificationRequest.class)))
                .thenReturn(
                        new NotificationResponse(
                                true,
                                "Success",
                                "Email"
                        )
                );

        retryService.retry(audit);

        verify(notificationServiceFactory)
                .getService(NotificationChannel.EMAIL);

        verify(notificationService)
                .sendNotification(any(NotificationRequest.class));
        verify(notificationService)
                .sendNotification(any(BroadcastNotificationRequest.class));
    }
}