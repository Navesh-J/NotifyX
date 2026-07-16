package com.navesh.notifyx.unit;

import com.navesh.notifyx.config.ProviderProperties;
import com.navesh.notifyx.core.NotificationChannel;
import com.navesh.notifyx.dto.NotificationRequest;
import com.navesh.notifyx.dto.NotificationResponse;
import com.navesh.notifyx.exception.NotificationDeliveryException;
import com.navesh.notifyx.gateway.PushGateway;
import com.navesh.notifyx.impl.push.PushNotificationService;
import com.navesh.notifyx.model.PushPayload;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PushNotificationServiceTest {

    @Mock
    private PushGateway pushGateway;

    @Mock
    private ProviderProperties providerProperties;

    @InjectMocks
    private PushNotificationService pushNotificationService;

    @Test
    void shouldSendPushSuccessfully() {

        when(providerProperties.getName())
                .thenReturn("Mock Push Gateway");

        NotificationRequest request = new NotificationRequest(
                NotificationChannel.PUSH,
                "device-token-123",
                "Hello from NotifyX"
        );

        NotificationResponse response =
                pushNotificationService.sendNotification(request);

        assertNotNull(response);
        assertTrue(response.success());

        assertEquals(
                "Mock Push Gateway",
                response.provider()
        );

        assertEquals(
                "Push Notification Sent Successfully",
                response.message()
        );

        ArgumentCaptor<PushPayload> captor =
                ArgumentCaptor.forClass(PushPayload.class);

        verify(pushGateway)
                .send(captor.capture());

        PushPayload payload = captor.getValue();

        assertEquals(
                "device-token-123",
                payload.deviceToken()
        );

        assertEquals(
                "Broadcast Notification",
                payload.title()
        );

        assertEquals(
                "Hello from NotifyX",
                payload.body()
        );

        verifyNoMoreInteractions(pushGateway);
    }

    @Test
    void shouldThrowNotificationDeliveryExceptionWhenGatewayFails() {

        when(providerProperties.getName())
                .thenReturn("Mock Push Gateway");

        doThrow(new RuntimeException("FCM Down"))
                .when(pushGateway)
                .send(any(PushPayload.class));

        NotificationRequest request = new NotificationRequest(
                NotificationChannel.PUSH,
                "device-token-123",
                "Hello"
        );

        NotificationDeliveryException exception =
                assertThrows(
                        NotificationDeliveryException.class,
                        () -> pushNotificationService.sendNotification(request)
                );

        assertEquals(
                "Unable to send push notification.",
                exception.getMessage()
        );

        verify(pushGateway)
                .send(any(PushPayload.class));

        verifyNoMoreInteractions(pushGateway);
    }
}