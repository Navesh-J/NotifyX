package com.navesh.notifyx.impl;

import com.navesh.notifyx.config.ProviderProperties;
import com.navesh.notifyx.core.NotificationChannel;
import com.navesh.notifyx.dto.BroadcastNotificationRequest;
import com.navesh.notifyx.dto.NotificationRequest;
import com.navesh.notifyx.dto.NotificationResponse;
import com.navesh.notifyx.core.NotificationService;
import com.navesh.notifyx.model.EmailPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailNotificationService implements NotificationService {

    private final ProviderProperties providerProperties;
    private final JavaMailSender mailSender;
    private static final Logger log =
            LoggerFactory.getLogger(EmailNotificationService.class);

    public EmailNotificationService(
            ProviderProperties providerProperties,
            JavaMailSender mailSender) {
        this.providerProperties = providerProperties;
        this.mailSender = mailSender;
    }

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

    @Override
    public NotificationResponse sendNotification(NotificationRequest request) {

        try {
            EmailPayload payload = buildPayload(request);

            sendMail(payload);

            log.info("Email successfully sent to {}",request.recipient());

            return new NotificationResponse(
                    true,
                    "Email sent successfully",
                    getProviderName()
            );
        } catch (Exception ex) {
            log.error("Failed to send email to {}",request.recipient(),ex);
            return new NotificationResponse(
                    false,
                    "Failed to send email to " + ex.getMessage(),
                    getProviderName()
            );
        }
    }

    @Override
    public NotificationResponse sendNotification(BroadcastNotificationRequest request){
        try{
            EmailPayload payload = buildPayload(request);

            sendMail(payload);

            log.info("Broadcast successfully sent to {}",request.recipient());

            return new NotificationResponse(
                    true,
                    "Broadcast sent successfully",
                    getProviderName()
            );
        } catch (Exception ex) {
            log.error("Failed to send broadcast email to {}", request.recipient(), ex);
            return new NotificationResponse(
                    false,
                    "Failed to send broadcast: " + ex.getMessage(),
                    getProviderName()
            );
        }
    }

    private void sendMail(EmailPayload payload) {
        SimpleMailMessage mail = new SimpleMailMessage();

        mail.setFrom(payload.from());
        mail.setTo(payload.to());
        mail.setSubject(payload.subject());
        mail.setText(payload.body());

        log.info("Sending email to {}", payload.to());

        mailSender.send(mail);
    }
}