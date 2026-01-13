package org.example.service;


import org.example.exceptions.NotificationException;
import org.example.exceptions.PaymentException;
import org.example.mapper.NotificationMapper;
import org.example.model.dto.NotificationRequest;
import org.example.model.dto.NotificationResponse;
import org.example.model.entities.Notification;
import org.example.model.entities.Payment;
import org.example.repository.NotificationRepository;
import org.example.repository.PaymentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private NotificationMapper notificationMapper;

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private NotificationService notificationService;

    // =========================
    @Test
    void createNotification_whenPaymentExists_savesAndReturnsResponse() {
        // Arrange
        NotificationRequest request = new NotificationRequest("Payment received", LocalDate.now(), 1L);
        Payment payment = new Payment();
        payment.setId(1L);
        payment.setAmount(100.0);
        payment.setPaymentDate(LocalDate.now());

        Notification notificationEntity = new Notification();
        notificationEntity.setMessage(request.message());
        notificationEntity.setNotificationDate(request.notificationDate());

        Notification savedNotification = new Notification();
        savedNotification.setId(100L);
        savedNotification.setMessage(request.message());
        savedNotification.setNotificationDate(request.notificationDate());
        savedNotification.setPayment(payment);

        NotificationResponse responseDto = new NotificationResponse();
        responseDto.setId(100L);
        responseDto.setMessage(request.message());
        responseDto.setNotificationDate(request.notificationDate());
        responseDto.setPaymentAmount(payment.getAmount());

        when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));
        when(notificationMapper.fromDtoToEntity(request)).thenReturn(notificationEntity);
        when(notificationRepository.save(notificationEntity)).thenReturn(savedNotification);
        when(notificationMapper.fromEntityToDto(savedNotification)).thenReturn(responseDto);

        // Act
        NotificationResponse result = notificationService.create(request);

        // Assert
        assertNotNull(result);
        assertEquals(100L, result.getId());
        assertEquals("Payment received", result.getMessage());

        verify(paymentRepository).findById(1L);
        verify(notificationMapper).fromDtoToEntity(request);
        verify(notificationRepository).save(notificationEntity);
        verify(notificationMapper).fromEntityToDto(savedNotification);
    }

    @Test
    void createNotification_whenPaymentNotFound_throwsPaymentException() {
        NotificationRequest request = NotificationRequest.builder()
                .message("Test Title").notificationDate(LocalDate.now()).paymentId(1L).build();


        when(paymentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(PaymentException.class, () -> notificationService.create(request));

        verify(notificationRepository, never()).save(any());
    }

    @Test
    void getByPaymentId_whenPaymentExists_returnsMappedList() {
        // Arrange
        Long paymentId = 1L;
        Payment payment = new Payment();
        payment.setId(paymentId);

        Notification n1 = new Notification();
        n1.setId(10L);
        Notification n2 = new Notification();
        n2.setId(11L);

        NotificationResponse r1 = new NotificationResponse();
        r1.setId(10L);
        NotificationResponse r2 = new NotificationResponse();
        r2.setId(11L);

        when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(payment));
        when(notificationRepository.findByPaymentId(paymentId)).thenReturn(List.of(n1, n2));
        when(notificationMapper.fromEntityToDto(n1)).thenReturn(r1);
        when(notificationMapper.fromEntityToDto(n2)).thenReturn(r2);

        // Act
        List<NotificationResponse> result = notificationService.getByPaymentId(paymentId);

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(r1));
        assertTrue(result.contains(r2));
        verify(paymentRepository).findById(paymentId);
        verify(notificationRepository).findByPaymentId(paymentId);
        verify(notificationMapper).fromEntityToDto(n1);
        verify(notificationMapper).fromEntityToDto(n2);
    }

    @Test
    void getByPaymentId_whenPaymentNotFound_throwsPaymentException() {
        // Arrange
        Long paymentId = 99L;
        when(paymentRepository.findById(paymentId)).thenReturn(Optional.empty());

        // Act & Assert
        PaymentException ex = assertThrows(PaymentException.class, () -> notificationService.getByPaymentId(paymentId));
        assertEquals("Payment not found", ex.getMessage());

        verify(notificationRepository, never()).findByPaymentId(any());
    }

    @Test
    void getById_whenNotificationExists_returnsMappedResponse() {
        // Arrange
        Long notificationId = 50L;
        Notification notification = new Notification();
        notification.setId(notificationId);

        NotificationResponse response = new NotificationResponse();
        response.setId(notificationId);

        when(notificationRepository.findById(notificationId)).thenReturn(Optional.of(notification));
        when(notificationMapper.fromEntityToDto(notification)).thenReturn(response);

        // Act
        NotificationResponse result = notificationService.getById(notificationId);

        // Assert
        assertNotNull(result);
        assertEquals(notificationId, result.getId());
        verify(notificationRepository).findById(notificationId);
        verify(notificationMapper).fromEntityToDto(notification);
    }

    @Test
    void getById_whenNotificationNotFound_throwsNotificationException() {
        // Arrange
        Long notificationId = 99L;
        when(notificationRepository.findById(notificationId)).thenReturn(Optional.empty());

        // Act & Assert
        NotificationException ex = assertThrows(NotificationException.class, () -> notificationService.getById(notificationId));
        assertEquals("Notification not found", ex.getMessage());

        verify(notificationMapper, never()).fromEntityToDto(any());
    }




}
