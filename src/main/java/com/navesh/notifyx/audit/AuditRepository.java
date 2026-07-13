package com.navesh.notifyx.audit;

import com.navesh.notifyx.core.AuditStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuditRepository extends JpaRepository<NotificationAuditLog, Long> {

    List<NotificationAuditLog> findByAuditStatus(AuditStatus auditStatus);
}
