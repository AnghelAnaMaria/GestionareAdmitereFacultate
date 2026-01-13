package org.example.model.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.model.entities.Notification;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponse {

    private Long id;
    private Double amount;
    private LocalDate paymentDate;
    private String candidateName;

    private List<NotificationResponse> notifications;
}
