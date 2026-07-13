package com.navesh.notifyx.audit;

import com.navesh.notifyx.core.AuditStatus;
import com.navesh.notifyx.dto.BroadcastNotificationRequest;
import com.navesh.notifyx.dto.ChannelResult;
import com.navesh.notifyx.dto.NotificationRequest;

public interface AuditService {

    void audit(
            NotificationRequest request,
            String provider,
            AuditStatus status,
            String errorMessage
    );

    void audit(
            BroadcastNotificationRequest request,
            ChannelResult result
    );
}
