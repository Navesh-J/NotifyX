package com.navesh.notifyx.audit;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditRepository extends JpaRepository<NotificationAuditLog, Long> {
}
