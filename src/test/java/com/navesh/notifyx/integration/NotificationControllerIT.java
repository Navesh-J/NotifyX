package com.navesh.notifyx.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.navesh.notifyx.controller.NotificationController;
import com.navesh.notifyx.core.NotificationChannel;
import com.navesh.notifyx.dto.*;
import com.navesh.notifyx.exception.NotificationDeliveryException;
import com.navesh.notifyx.service.NotificationApplicationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NotificationController.class)
class NotificationControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private NotificationApplicationService applicationService;

    @Test
    void shouldSendNotificationSuccessfully() throws Exception {

        NotificationRequest request =
                new NotificationRequest(
                        NotificationChannel.EMAIL,
                        "john@example.com",
                        "Hello NotifyX"
                );

        NotificationResponse response =
                new NotificationResponse(
                        true,
                        "Email sent successfully",
                        "Email Notification Service"
                );

        when(applicationService.sendNotification(any(NotificationRequest.class)))
                .thenReturn(response);

        mockMvc.perform(
                        post("/api/notifications/send")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(request)
                                )
                )

                .andExpect(status().isOk())

                .andExpect(jsonPath("$.success").value(true))

                .andExpect(jsonPath("$.message")
                        .value("Email sent successfully"))

                .andExpect(jsonPath("$.provider")
                        .value("Email Notification Service"));
    }

    @Test
    void shouldReturnBadRequestForInvalidRequest() throws Exception {

        NotificationRequest request =
                new NotificationRequest(
                        NotificationChannel.EMAIL,
                        "",
                        ""
                );

        mockMvc.perform(
                        post("/api/notifications/send")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(request)
                                )
                )

                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnInternalServerErrorWhenServiceFails() throws Exception {

        NotificationRequest request =
                new NotificationRequest(
                        NotificationChannel.EMAIL,
                        "john@example.com",
                        "Hello"
                );

        when(applicationService.sendNotification(any(NotificationRequest.class)))

                .thenThrow(
                        new NotificationDeliveryException(
                                "SMTP Down"
                        )
                );

        mockMvc.perform(
                        post("/api/notifications/send")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(request)
                                )
                )

                .andExpect(status().isInternalServerError())

                .andExpect(jsonPath("$.error")
                        .value("Notification Delivery Failed"));
    }

    @Test
    void shouldBroadcastNotificationSuccessfully() throws Exception {

        BroadcastNotificationRequest request =
                new BroadcastNotificationRequest(
                        "john@example.com",
                        "Broadcast Message"
                );

        BroadcastNotificationResponse response =
                new BroadcastNotificationResponse(
                        3,
                        3,
                        0,
                        List.of(
                                new ChannelResult(
                                        NotificationChannel.EMAIL,
                                        "Email",
                                        true,
                                        "Email Sent"
                                ),
                                new ChannelResult(
                                        NotificationChannel.SMS,
                                        "SMS",
                                        true,
                                        "SMS Sent"
                                ),
                                new ChannelResult(
                                        NotificationChannel.PUSH,
                                        "Push",
                                        true,
                                        "Push Sent"
                                )
                        ),
                        LocalDateTime.now()
                );

        when(applicationService
                .sendNotificationToAll(any(BroadcastNotificationRequest.class)))
                .thenReturn(response);

        mockMvc.perform(
                        post("/api/notifications/send-all")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(request)
                                )
                )

                .andExpect(status().isOk());
    }
}