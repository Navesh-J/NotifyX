package com.navesh.notifyx.unit;

import com.navesh.notifyx.core.NotificationChannel;
import com.navesh.notifyx.core.NotificationService;
import com.navesh.notifyx.dto.BroadcastNotificationRequest;
import com.navesh.notifyx.dto.BroadcastNotificationResponse;
import com.navesh.notifyx.dto.NotificationResponse;
import com.navesh.notifyx.exception.NotificationDeliveryException;
import com.navesh.notifyx.impl.CompositeNotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompositeNotificationServiceTest {

    @Mock
    private NotificationService emailService;

    @Mock
    private NotificationService smsService;

    @Mock
    private NotificationService pushService;

    @InjectMocks
    private CompositeNotificationService compositeNotificationService;

    @BeforeEach
    void setUp() {
        compositeNotificationService =
                new CompositeNotificationService(
                        List.of(emailService, smsService, pushService)
                );
    }

    @Test
    void shouldSendNotificationToAllChannels() {

        BroadcastNotificationRequest request =
                new BroadcastNotificationRequest(
                        "navesh@example.com",
                        "Hello NotifyX"
                );

        when(emailService.getSupportedChannel()).thenReturn(NotificationChannel.EMAIL);
        when(smsService.getSupportedChannel()).thenReturn(NotificationChannel.SMS);
        when(pushService.getSupportedChannel()).thenReturn(NotificationChannel.PUSH);
        when(emailService.getProviderName()).thenReturn("Email");
        when(smsService.getProviderName()).thenReturn("SMS");
        when(pushService.getProviderName()).thenReturn("Push");

        when(emailService.sendNotification(request))
                .thenReturn(new NotificationResponse(
                        true,
                        "Email Sent",
                        "Email"
                ));

        when(smsService.sendNotification(request))
                .thenReturn(new NotificationResponse(
                        true,
                        "SMS Sent",
                        "SMS"
                ));

        when(pushService.sendNotification(request))
                .thenReturn(new NotificationResponse(
                        true,
                        "Push Sent",
                        "Push"
                ));

        BroadcastNotificationResponse response =
                compositeNotificationService.sendToAll(request);
        assertNotNull(response);

        assertEquals(3, response.totalChannels());

        assertEquals(3, response.successfulChannels());

        assertEquals(0, response.failedChannels());

        assertEquals(3, response.results().size());

        verify(emailService).sendNotification(request);

        verify(smsService).sendNotification(request);

        verify(pushService).sendNotification(request);

        verifyNoMoreInteractions(
                emailService,
                smsService,
                pushService
        );
    }

    @Test
    void shouldContinueWhenOneChannelFails() {

        BroadcastNotificationRequest request =
                new BroadcastNotificationRequest(
                        "john@example.com",
                        "Hello NotifyX"
                );

        when(emailService.getSupportedChannel())
                .thenReturn(NotificationChannel.EMAIL);

        when(smsService.getSupportedChannel())
                .thenReturn(NotificationChannel.SMS);

        when(pushService.getSupportedChannel())
                .thenReturn(NotificationChannel.PUSH);

        when(emailService.getProviderName())
                .thenReturn("Email");

        when(smsService.getProviderName())
                .thenReturn("SMS");

        when(pushService.getProviderName())
                .thenReturn("Push");

        when(emailService.sendNotification(request))
                .thenReturn(
                        new NotificationResponse(
                                true,
                                "Email Sent",
                                "Email"
                        )
                );

        doThrow(
                new NotificationDeliveryException(
                        "SMS Gateway Down"
                )
        ).when(smsService)
                .sendNotification(request);

        when(pushService.sendNotification(request))
                .thenReturn(
                        new NotificationResponse(
                                true,
                                "Push Sent",
                                "Push"
                        )
                );

        BroadcastNotificationResponse response =
                compositeNotificationService.sendToAll(request);

        assertEquals(
                2,
                response.successfulChannels()
        );

        assertEquals(
                1,
                response.failedChannels()
        );

        assertEquals(
                3,
                response.results().size()
        );

        verify(emailService).sendNotification(request);

        verify(smsService).sendNotification(request);

        verify(pushService).sendNotification(request);
    }

    @Test
    void shouldHandleAllChannelsFailing() {

        BroadcastNotificationRequest request =
                new BroadcastNotificationRequest(
                        "john@example.com",
                        "Hello NotifyX"
                );

        when(emailService.getSupportedChannel())
                .thenReturn(NotificationChannel.EMAIL);

        when(smsService.getSupportedChannel())
                .thenReturn(NotificationChannel.SMS);

        when(pushService.getSupportedChannel())
                .thenReturn(NotificationChannel.PUSH);

        when(emailService.getProviderName())
                .thenReturn("Email");

        when(smsService.getProviderName())
                .thenReturn("SMS");

        when(pushService.getProviderName())
                .thenReturn("Push");

        doThrow(new NotificationDeliveryException("Email Failed"))
                .when(emailService)
                .sendNotification(request);

        doThrow(new NotificationDeliveryException("SMS Failed"))
                .when(smsService)
                .sendNotification(request);

        doThrow(new NotificationDeliveryException("Push Failed"))
                .when(pushService)
                .sendNotification(request);

        BroadcastNotificationResponse response =
                compositeNotificationService.sendToAll(request);

        assertEquals(0, response.successfulChannels());

        assertEquals(3, response.failedChannels());

        assertEquals(3, response.results().size());
    }
}