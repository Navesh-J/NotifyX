package com.navesh.notifyx.unit;

import com.navesh.notifyx.core.NotificationChannel;
import com.navesh.notifyx.core.NotificationService;
import com.navesh.notifyx.exception.ChannelUnavailableException;
import com.navesh.notifyx.factory.NotificationServiceFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificationServiceFactoryTest {

    @Mock
    private NotificationService emailService;

    @Mock
    private NotificationService smsService;

    private NotificationServiceFactory factory;

    @BeforeEach
    void setUp() {

        when(emailService.supports(NotificationChannel.EMAIL)).thenReturn(true);

        when(emailService.supports(NotificationChannel.SMS)).thenReturn(false);

        when(smsService.supports(NotificationChannel.SMS)).thenReturn(true);

        when(smsService.supports(NotificationChannel.EMAIL)).thenReturn(false);

        factory = new NotificationServiceFactory(
                List.of(emailService,smsService)
        );
    }

    @Test
    void shouldReturnEmailService() {
        NotificationService service = factory.getService(NotificationChannel.EMAIL);
        assertEquals(emailService,service);
    }

    @Test
    void shouldReturnSmsService() {
        NotificationService service = factory.getService(NotificationChannel.SMS);
        assertEquals(smsService,service);
    }

    @Test
    void shouldThrowExceptionWhenClassNotSupported() {
        assertThrows(
                ChannelUnavailableException.class,
                () -> factory.getService(NotificationChannel.PUSH)
        );
    }
}