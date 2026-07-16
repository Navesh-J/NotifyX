package com.navesh.notifyx.unit;

import com.navesh.notifyx.config.ProviderProperties;
import com.navesh.notifyx.core.NotificationChannel;
import com.navesh.notifyx.dto.NotificationRequest;
import com.navesh.notifyx.dto.NotificationResponse;
import com.navesh.notifyx.exception.NotificationDeliveryException;
import com.navesh.notifyx.gateway.SmsGateway;
import com.navesh.notifyx.impl.sms.SmsNotificationService;
import com.navesh.notifyx.model.SmsPayload;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SmsNotificationServiceTest {

    @Mock
    private SmsGateway smsGateway;

    @Mock
    private ProviderProperties providerProperties;

    @InjectMocks
    private SmsNotificationService smsNotificationService;

    @Test
    void shouldSendSmsSuccessfully() {

        when(providerProperties.getName())
                .thenReturn("Mock SMS Gateway");

        NotificationRequest request = new NotificationRequest(
                NotificationChannel.SMS,
                "9876543210",
                "Hello from NotifyX"
        );

        NotificationResponse response =
                smsNotificationService.sendNotification(request);

        assertNotNull(response);
        assertTrue(response.success());

        assertEquals(
                "Mock SMS Gateway",
                response.provider()
        );

        assertEquals(
                "SMS Sent Successfully",
                response.message()
        );

        ArgumentCaptor<SmsPayload> captor =
                ArgumentCaptor.forClass(SmsPayload.class);

        verify(smsGateway).send(captor.capture());

        SmsPayload payload = captor.getValue();

        assertEquals(
                "9876543210",
                payload.phoneNumber()
        );

        assertEquals(
                "Hello from NotifyX",
                payload.message()
        );

        verifyNoMoreInteractions(smsGateway);
    }

    @Test
    void shouldThrowNotificationDeliveryExceptionWhenGatewayFails() {

        when(providerProperties.getName())
                .thenReturn("Mock SMS Gateway");

        doThrow(new RuntimeException("Gateway Down"))
                .when(smsGateway)
                .send(any(SmsPayload.class));

        NotificationRequest request = new NotificationRequest(
                NotificationChannel.SMS,
                "9876543210",
                "Hello"
        );

        NotificationDeliveryException exception =
                assertThrows(
                        NotificationDeliveryException.class,
                        () -> smsNotificationService.sendNotification(request)
                );

        assertEquals(
                "Unable to send SMS.",
                exception.getMessage()
        );

        verify(smsGateway)
                .send(any(SmsPayload.class));

        verifyNoMoreInteractions(smsGateway);
    }
}