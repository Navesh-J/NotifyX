package com.navesh.notifyx.impl.email;

import com.navesh.notifyx.config.ProviderProperties;
import com.navesh.notifyx.core.NotificationChannel;
import com.navesh.notifyx.dto.BroadcastNotificationRequest;
import com.navesh.notifyx.dto.NotificationRequest;
import com.navesh.notifyx.dto.NotificationResponse;
import com.navesh.notifyx.core.NotificationService;
import com.navesh.notifyx.exception.NotificationDeliveryException;
import com.navesh.notifyx.model.EmailPayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailNotificationService implements NotificationService {

    private final ProviderProperties providerProperties;
    private final JavaMailSender mailSender;

    @Override
    public boolean supports(NotificationChannel channel) {
        return channel == NotificationChannel.EMAIL;
    }

    @Override
    public NotificationChannel getSupportedChannel() {
        return NotificationChannel.EMAIL;
    }

    @Override
    public String getProviderName() {
        return providerProperties.getName();
    }

    private EmailPayload buildPayload(NotificationRequest request) {
        return new EmailPayload(
                "noreply@notifyx.dev",
                request.recipient(),
                "Notifyx Test mail",
                request.message()
        );
    }

    private EmailPayload buildPayload(BroadcastNotificationRequest request) {
        return new EmailPayload(
                "noreply@notifyx.dev",
                request.recipient(),
                "Broadcast Notification",
                request.message()
        );
    }

    @Retryable(
            retryFor = NotificationDeliveryException.class,
            maxAttempts = 3,
            backoff = @Backoff(
                    delay = 1000,
                    multiplier = 2
            )
    )
    @Override
    public NotificationResponse sendNotification(NotificationRequest request) {

        try {
            EmailPayload payload = buildPayload(request);

            sendMail(payload);

            return new NotificationResponse(
                    true,
                    "Email sent successfully",
                    getProviderName()
            );
        } catch (Exception ex) {
            throw new NotificationDeliveryException(
                    "Unable to send email.",
                    ex
            );
        }
    }

    @Retryable(
            retryFor = NotificationDeliveryException.class,
            maxAttempts = 3,
            backoff = @Backoff(
                    delay = 1000,
                    multiplier = 2
            )
    )
    @Override
    public NotificationResponse sendNotification(BroadcastNotificationRequest request) {
        try {
            EmailPayload payload = buildPayload(request);

            sendMail(payload);

            return new NotificationResponse(
                    true,
                    "Broadcast EMAIL sent successfully",
                    getProviderName()
            );
        } catch (Exception ex) {
            throw new NotificationDeliveryException(
                    "Unable to send broadcast email.",
                    ex
            );
        }
    }

    private void sendMail(EmailPayload payload) {
        SimpleMailMessage mail = new SimpleMailMessage();

        mail.setFrom(payload.from());
        mail.setTo(payload.to());
        mail.setSubject(payload.subject());
        mail.setText(payload.body());

        mailSender.send(mail);
    }

    @Recover
    public NotificationResponse recover(
            NotificationDeliveryException ex,
            NotificationRequest request) {

        log.error(
                "Email delivery permanently failed for {}",
                request.recipient(),
                ex
        );

        throw ex;
    }

    @Recover
    public NotificationResponse recover(
            NotificationDeliveryException ex,
            BroadcastNotificationRequest request) {

        log.error(
                "Broadcast email permanently failed for {}",
                request.recipient(),
                ex
        );

        throw ex;
    }
}