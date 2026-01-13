package org.example.model.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record PaymentRequest (@NotNull(message = "Amount is required")
                              Double amount,

                              @NotNull(message = "Payment date is required")
                              LocalDate paymentDate,

                              @NotNull(message = "Candidate id cannot be null")
                              Long candidateId){
}
