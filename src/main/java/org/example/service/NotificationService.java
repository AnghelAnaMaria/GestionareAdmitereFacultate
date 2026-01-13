package org.example.service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.exceptions.NotificationException;
import org.example.exceptions.PaymentException;
import org.example.mapper.NotificationMapper;
import org.example.model.dto.NotificationRequest;
import org.example.model.dto.NotificationResponse;
import org.example.model.entities.Notification;
import org.example.model.entities.Payment;
import org.example.repository.NotificationRepository;
import org.example.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;
    private final PaymentRepository paymentRepository;

    @Transactional
    public NotificationResponse create(NotificationRequest request){
        Payment payment= paymentRepository.findById(request.paymentId()).orElseThrow(() -> new PaymentException("Payment not found"));

        Notification notification= notificationMapper.fromDtoToEntity(request);//creez Notification fara FK catre Payment
        notification.setPayment(payment); //adaug Payment

        Notification saved= notificationRepository.save(notification);
        log.info("Notification {} saved for Payment {}", saved, payment);
        return notificationMapper.fromEntityToDto(saved);
    }

    public List<NotificationResponse> getByPaymentId(Long paymentId){
        paymentRepository.findById(paymentId).orElseThrow(() -> new PaymentException("Payment not found"));
        List<Notification> notifications= notificationRepository.findByPaymentId(paymentId);

        return notifications.stream().map(notificationMapper::fromEntityToDto).toList();
    }

    public NotificationResponse getById(Long id){
        log.info("Fetching notification with id {}", id);
        Notification notification= notificationRepository.findById(id).orElseThrow(() -> new NotificationException("Notification not found"));

        return notificationMapper.fromEntityToDto(notification);
    }

    public List<String> getMessagesForHighPayments(Double minAmount) {
        return notificationRepository.findMessagesByPaymentAmountGreaterThanEqual(minAmount);
    }
}
