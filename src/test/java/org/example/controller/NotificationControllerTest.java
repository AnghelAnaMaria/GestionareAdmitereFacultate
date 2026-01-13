package org.example.controller;


import org.example.model.dto.NotificationRequest;
import org.example.model.dto.NotificationResponse;
import org.example.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class NotificationControllerTest {

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private NotificationController notificationController;

    private NotificationRequest sampleRequest;
    private NotificationResponse sampleResponse;

    @BeforeEach
    void setUp() {
        sampleRequest = new NotificationRequest("Payment received", LocalDate.now(), 1L);
        sampleResponse = new NotificationResponse(1L, "Payment received", LocalDate.now(), 100.0);
    }

    @Test
    void create_returnsCreatedNotificationResponse() {
        when(notificationService.create(sampleRequest)).thenReturn(sampleResponse);

        ResponseEntity<NotificationResponse> result = notificationController.create(sampleRequest);

        assertEquals(201, result.getStatusCode().value());
        assertEquals(sampleResponse, result.getBody());
        verify(notificationService).create(sampleRequest);
    }

    @Test
    void getByPaymentId_returnsListOfNotifications() {
        when(notificationService.getByPaymentId(1L)).thenReturn(List.of(sampleResponse));

        ResponseEntity<List<NotificationResponse>> result = notificationController.getByPaymentId(1L);

        assertEquals(200, result.getStatusCode().value());
        assertEquals(1, result.getBody().size());
        assertTrue(result.getBody().contains(sampleResponse));
        verify(notificationService).getByPaymentId(1L);
    }

    @Test
    void getById_returnsNotification() {
        when(notificationService.getById(1L)).thenReturn(sampleResponse);

        ResponseEntity<NotificationResponse> result = notificationController.getById(1L);

        assertEquals(200, result.getStatusCode().value());
        assertEquals(sampleResponse, result.getBody());
        verify(notificationService).getById(1L);
    }


}
