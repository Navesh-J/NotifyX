package com.navesh.notifyx.audit;

import com.navesh.notifyx.core.AuditStatus;
import com.navesh.notifyx.core.NotificationChannel;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "notification_audit")
public class NotificationAuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private NotificationChannel channel;

    @Column(nullable = false)
    private String provider;

    @Column(nullable = false)
    private String recipient;

    @Column(nullable = false, length = 1000)
    private String message;

    @Enumerated(EnumType.STRING)
    private AuditStatus auditStatus;

    @Lob
    @Column(name = "error_message")
    private String errorMessage;

    private LocalDateTime sentAt;

    private int retryCount;

    private LocalDateTime nextRetryAt;

}
