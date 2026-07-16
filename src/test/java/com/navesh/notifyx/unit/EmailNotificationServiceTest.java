package com.navesh.notifyx.unit;

import com.navesh.notifyx.config.ProviderProperties;
import com.navesh.notifyx.core.NotificationChannel;
import com.navesh.notifyx.dto.NotificationRequest;
import com.navesh.notifyx.dto.NotificationResponse;
import com.navesh.notifyx.exception.NotificationDeliveryException;
import com.navesh.notifyx.impl.email.EmailNotificationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailNotificationServiceTest {

    @Mock
    private ProviderProperties providerProperties;

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailNotificationService emailNotificationService;

    @Test
    void shouldSendEmailSuccessfully() {
        when(providerProperties.getName()).thenReturn("Email Notification Service");

        NotificationRequest request =
                new NotificationRequest(
                        NotificationChannel.EMAIL,
                        "navesh@example.com",
                        "Hello from NotifyX"
                );

        NotificationResponse response = emailNotificationService.sendNotification(request);

        assertNotNull(response);
        assertTrue(response.success());
        assertEquals("Email Notification Service", response.provider());
        assertEquals("Email sent successfully", response.message());

        ArgumentCaptor<SimpleMailMessage> captor =
                ArgumentCaptor.forClass(SimpleMailMessage.class);

        verify(mailSender).send(captor.capture());
        SimpleMailMessage mail = captor.getValue();
        assertNotNull(mail);

        assertEquals("noreply@notifyx.dev", mail.getFrom());
        assertArrayEquals(
                new String[]{"navesh@example.com"},
                mail.getTo()
        );

        assertEquals(
                "Notifyx Test mail",
                mail.getSubject()
        );

        assertEquals(
                "Hello from NotifyX",
                mail.getText()
        );

        verifyNoMoreInteractions(mailSender);
    }

    @Test
    void shouldThrowExceptionWhenMailSendingFails() {
        when(providerProperties.getName()).thenReturn("Email Notification Service");

        doThrow(new MailSendException("SMTP Down"))
                .when(mailSender)
                .send(any(SimpleMailMessage.class));

        NotificationRequest request =
                new NotificationRequest(
                        NotificationChannel.EMAIL,
                        "navesh@example.com",
                        "Hello from NotifyX"
                );

        NotificationDeliveryException exception =
                assertThrows(
                        NotificationDeliveryException.class,
                        () -> emailNotificationService.sendNotification(request)
                );

        assertNotNull(exception);
        assertEquals(
                "Unable to send mail",
                exception.getMessage()
        );
        verify(mailSender).send(any(SimpleMailMessage.class));
        verifyNoMoreInteractions(mailSender);
    }
}