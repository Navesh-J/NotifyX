package com.navesh.notifyx.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.navesh.notifyx.audit.AuditRepository;
import com.navesh.notifyx.core.NotificationChannel;
import com.navesh.notifyx.core.AuditStatus;
import com.navesh.notifyx.dto.NotificationRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("sms")
class NotificationEndToEndIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AuditRepository auditRepository;

    @BeforeEach
    void setUp() {
        auditRepository.deleteAll();
    }

    @Test
    void shouldPersistAuditRecordAfterSuccessfulNotification() throws Exception {

        NotificationRequest request =
                new NotificationRequest(
                        NotificationChannel.SMS,
                        "9876543210",
                        "Hello NotifyX"
                );

        mockMvc.perform(
                        post("/api/notifications/send")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(request)
                                )
                )
                .andExpect(status().isOk());

        assertEquals(
                1,
                auditRepository.count()
        );

        var audit =
                auditRepository.findAll().get(0);

        assertEquals(
                NotificationChannel.SMS,
                audit.getChannel()
        );

        assertEquals(
                AuditStatus.SUCCESS,
                audit.getAuditStatus()
        );

        assertEquals(
                "9876543210",
                audit.getRecipient()
        );

        assertEquals(
                "Hello NotifyX",
                audit.getMessage()
        );
    }
}