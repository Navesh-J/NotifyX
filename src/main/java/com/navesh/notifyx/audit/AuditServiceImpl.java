package com.navesh.notifyx.audit;

import com.navesh.notifyx.core.AuditStatus;
import com.navesh.notifyx.core.NotificationChannel;
import com.navesh.notifyx.core.NotificationStatus;
import com.navesh.notifyx.dto.BroadcastNotificationRequest;
import com.navesh.notifyx.dto.ChannelResult;
import com.navesh.notifyx.dto.NotificationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class AuditServiceImpl implements AuditService {

    private final AuditRepository auditRepository;

    @Override
    public void audit(
            NotificationRequest request,
            String provider,
            AuditStatus status,
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

        auditRepository.save(auditLog);

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
                result.success() ? AuditStatus.SUCCESS : AuditStatus.RETRY_PENDING,
                result.success() ? null : result.message()
        );

        auditRepository.save(auditLog);

    }

    private NotificationAuditLog buildAuditLog(
            NotificationChannel channel,
            String provider,
            String recipient,
            String message,
            AuditStatus status,
            String errorMessage) {

        return NotificationAuditLog.builder()
                .channel(channel)
                .provider(provider)
                .recipient(recipient)
                .message(message)
                .auditStatus(status)
                .retryCount(0)
                .nextRetryAt(null)
                .errorMessage(errorMessage)
                .sentAt(LocalDateTime.now())
                .build();
    }
}
