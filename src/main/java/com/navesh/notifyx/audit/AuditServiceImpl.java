package com.navesh.notifyx.audit;

import com.navesh.notifyx.core.NotificationStatus;
import com.navesh.notifyx.dto.BroadcastNotificationRequest;
import com.navesh.notifyx.dto.ChannelResult;
import com.navesh.notifyx.dto.NotificationRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuditServiceImpl implements AuditService {

    private final AuditRepository auditRepository;

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
        NotificationAuditLog auditLog = new NotificationAuditLog();

        auditLog.setChannel(request.channel());
        auditLog.setProvider(provider);
        auditLog.setRecipient(request.recipient());
        auditLog.setMessage(request.message());
        auditLog.setStatus(status);
        auditLog.setErrorMessage(errorMessage);
        auditLog.setSentAt(LocalDateTime.now());

        auditRepository.save(auditLog);
    }

    @Override
    public void audit(
            BroadcastNotificationRequest request,
            ChannelResult result
    ) {
        NotificationAuditLog auditLog = new NotificationAuditLog();
        auditLog.setProvider(result.provider());
        auditLog.setRecipient(request.recipient());
        auditLog.setMessage(request.message());


        auditLog.setStatus(
                result.success() ? NotificationStatus.SUCCESS : NotificationStatus.FAILED
        );


        auditLog.setErrorMessage(
                result.success() ? null : result.message()
        );

        auditLog.setSentAt(LocalDateTime.now());

        auditRepository.save(auditLog);
    }
}
