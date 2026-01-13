package org.example.mapper;

import org.example.model.dto.NotificationResponse;
import org.example.model.dto.PaymentRequest;
import org.example.model.dto.PaymentResponse;
import org.example.model.entities.Payment;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class PaymentMapper {

    public PaymentResponse fromEntityToDto(Payment payment) {
        return new PaymentResponse(
                payment.getId(),
                payment.getAmount(),
                payment.getPaymentDate(),
                payment.getCandidate().getName(),
                payment.getNotifications() != null
                        ? payment.getNotifications().stream().map(n -> new NotificationResponse(n.getId(), n.getMessage(), n.getNotificationDate(), n.getPayment().getAmount())).collect(Collectors.toList())
                        : null
        );
    }

    public Payment fromDtoToEntity(PaymentRequest dto) {
        Payment payment = new Payment();
        //Candidate adaugat in service
        payment.setAmount(dto.amount());
        payment.setPaymentDate(dto.paymentDate());
        return payment;
    }
}
