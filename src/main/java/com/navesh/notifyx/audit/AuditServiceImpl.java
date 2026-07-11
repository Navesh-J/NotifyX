package com.navesh.notifyx.audit;

import com.navesh.notifyx.core.NotificationChannel;
import com.navesh.notifyx.core.NotificationStatus;
import com.navesh.notifyx.dto.BroadcastNotificationRequest;
import com.navesh.notifyx.dto.ChannelResult;
import com.navesh.notifyx.dto.NotificationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuditServiceImpl implements AuditService {

    private final AuditRepository auditRepository;
    private static final Logger log =
            LoggerFactory.getLogger(AuditServiceImpl.class);

    public AuditServiceImpl(AuditRepository auditRepository) {
        this.auditRepository = auditRepository;
    }

    @Override
    public void audit(
            NotificationRequest request,
            String provider,
            NotificationStatus status,
            String errorMessage
    ) {
        NotificationAuditLog auditLog = buildAuditLog(
                request.channel(),
                provider,
                request.recipient(),
                request.message(),
                status,
                errorMessage
        );

        log.debug(
                "Persisting audit log for provider {}",
                provider
        );

        auditRepository.save(auditLog);

        log.debug(
                "Audit record saved successfully."
        );
    }

    @Override
    public void audit(
            BroadcastNotificationRequest request,
            ChannelResult result
    ) {
        NotificationAuditLog auditLog = buildAuditLog(
                result.notificationChannel(),
                result.provider(),
                request.recipient(),
                request.message(),
                result.success() ? NotificationStatus.SUCCESS : NotificationStatus.FAILED,
                result.success() ? null : result.message()
        );

        log.debug(
                "Persisting audit log for broadcast provider {}",
                result.provider()
        );

        auditRepository.save(auditLog);

        log.debug(
                "Broadcast Audit record saved successfully."
        );
    }

    private NotificationAuditLog buildAuditLog(
            NotificationChannel channel,
            String provider,
            String recipient,
            String message,
            NotificationStatus status,
            String errorMessage) {

        return NotificationAuditLog.builder()
                .channel(channel)
                .provider(provider)
                .recipient(recipient)
                .message(message)
                .status(status)
                .errorMessage(errorMessage)
                .sentAt(LocalDateTime.now())
                .build();
    }
}
