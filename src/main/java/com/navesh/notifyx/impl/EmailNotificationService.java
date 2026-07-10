package com.navesh.notifyx.impl;

import com.navesh.notifyx.config.ProviderProperties;
import com.navesh.notifyx.core.NotificationChannel;
import com.navesh.notifyx.dto.BroadcastNotificationRequest;
import com.navesh.notifyx.dto.NotificationRequest;
import com.navesh.notifyx.dto.NotificationResponse;
import com.navesh.notifyx.core.NotificationService;
import com.navesh.notifyx.model.EmailPayload;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
//@Profile("email")
public class EmailNotificationService implements NotificationService {

    private final ProviderProperties providerProperties;
    private final JavaMailSender mailSender;

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

    @Override
    public NotificationResponse sendNotification(NotificationRequest request) {

        EmailPayload payload = buildPayload(request);

        SimpleMailMessage mail = new SimpleMailMessage();

        mail.setFrom(payload.from());
        mail.setTo(payload.to());
        mail.setSubject(payload.subject());
        mail.setText(payload.body());

        mailSender.send(mail);

        return new NotificationResponse(
                true,
                "Email sent successfully",
                getProviderName()
        );
    }

    @Override
    public NotificationResponse sendNotification(BroadcastNotificationRequest request){
        EmailPayload payload = new EmailPayload(
                "noreply@notifyx.dev",
                request.recipient(),
                "Broadcast Notification",
                request.message()
        );

        SimpleMailMessage mail = new SimpleMailMessage();

        mail.setFrom(payload.from());
        mail.setTo(payload.to());
        mail.setSubject(payload.subject());
        mail.setText(payload.body());

        mailSender.send(mail);

        return new NotificationResponse(
                true,
                "Broadcast sent successfully",
                getProviderName()
        );
    }
}