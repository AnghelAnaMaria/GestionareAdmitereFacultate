package org.example.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record NotificationRequest (@NotBlank(message = "Message is required")
                                   String message,

                                   @NotNull(message = "Notification date is required")
                                   LocalDate notificationDate,

                                   @NotNull(message = "Payment ID is required")
                                   Long paymentId){
}
