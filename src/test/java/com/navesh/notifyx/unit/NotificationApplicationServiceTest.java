package com.navesh.notifyx.unit;

import com.navesh.notifyx.audit.AuditService;
import com.navesh.notifyx.core.AuditStatus;
import com.navesh.notifyx.core.NotificationChannel;
import com.navesh.notifyx.core.NotificationService;
import com.navesh.notifyx.dto.NotificationRequest;
import com.navesh.notifyx.dto.NotificationResponse;
import com.navesh.notifyx.exception.NotificationDeliveryException;
import com.navesh.notifyx.factory.NotificationServiceFactory;
import com.navesh.notifyx.service.NotificationApplicationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationApplicationServiceTest {

    @Mock
    private NotificationServiceFactory notificationServiceFactory;

    @Mock
    private NotificationService notificationService;

    @Mock
    private AuditService auditService;

    @InjectMocks
    private NotificationApplicationService applicationService;

    @Test
    void shouldSendNotificationSuccessfully() {

        NotificationRequest request = new NotificationRequest(
                NotificationChannel.EMAIL,
                "john@example.com",
                "Hello NotifyX"
        );

        NotificationResponse expected =
                new NotificationResponse(
                        true,
                        "Email sent successfully",
                        "Email"
                );

        when(notificationServiceFactory.getService(NotificationChannel.EMAIL))
                .thenReturn(notificationService);

        when(notificationService.sendNotification(request))
                .thenReturn(expected);

        NotificationResponse response =
                applicationService.sendNotification(request);

        assertNotNull(response);

        assertEquals(expected, response);

        verify(notificationServiceFactory)
                .getService(NotificationChannel.EMAIL);

        verify(notificationService)
                .sendNotification(request);
    }

    @Test
    void shouldAuditSuccessfulNotification() {

        NotificationRequest request = new NotificationRequest(
                NotificationChannel.EMAIL,
                "john@example.com",
                "Hello"
        );

        NotificationResponse response =
                new NotificationResponse(
                        true,
                        "Success",
                        "Email"
                );

        when(notificationServiceFactory.getService(NotificationChannel.EMAIL))
                .thenReturn(notificationService);

        when(notificationService.sendNotification(request))
                .thenReturn(response);

        applicationService.sendNotification(request);

        verify(auditService)
                .audit(
                        eq(request),
                        eq("Email"),
                        eq(AuditStatus.SUCCESS),
                        isNull()
                );
    }

    @Test
    void shouldAuditFailedNotification() {

        NotificationRequest request = new NotificationRequest(
                NotificationChannel.EMAIL,
                "john@example.com",
                "Hello"
        );

        NotificationDeliveryException exception =
                new NotificationDeliveryException(
                        "SMTP Down"
                );

        when(notificationServiceFactory.getService(NotificationChannel.EMAIL))
                .thenReturn(notificationService);

        when(notificationService.sendNotification(request))
                .thenThrow(exception);

        assertThrows(
                NotificationDeliveryException.class,
                () -> applicationService.sendNotification(request)
        );

        verify(auditService)
                .audit(
                        eq(request),
                        anyString(),
                        eq(AuditStatus.RETRY_PENDING),
                        eq("SMTP Down")
                );
    }

    @Test
    void shouldPropagateNotificationException() {

        NotificationRequest request = new NotificationRequest(
                NotificationChannel.EMAIL,
                "john@example.com",
                "Hello"
        );

        when(notificationServiceFactory.getService(NotificationChannel.EMAIL))
                .thenReturn(notificationService);

        when(notificationService.sendNotification(request))
                .thenThrow(
                        new NotificationDeliveryException(
                                "SMTP Down"
                        )
                );

        assertThrows(
                NotificationDeliveryException.class,
                () -> applicationService.sendNotification(request)
        );
    }

    @Test
    void shouldNotAuditSuccessWhenNotificationFails() {

        NotificationRequest request = new NotificationRequest(
                NotificationChannel.EMAIL,
                "john@example.com",
                "Hello"
        );

        when(notificationServiceFactory.getService(NotificationChannel.EMAIL))
                .thenReturn(notificationService);

        when(notificationService.sendNotification(request))
                .thenThrow(
                        new NotificationDeliveryException(
                                "SMTP Down"
                        )
                );

        assertThrows(
                NotificationDeliveryException.class,
                () -> applicationService.sendNotification(request)
        );

        verify(auditService, never())
                .audit(
                        eq(request),
                        anyString(),
                        eq(AuditStatus.SUCCESS),
                        any()
                );
    }
}