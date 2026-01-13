package org.example.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.model.dto.ExamResponse;
import org.example.model.dto.PaymentRequest;
import org.example.model.dto.PaymentResponse;
import org.example.model.entities.Payment;
import org.example.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentResponse> create(@Valid @RequestBody PaymentRequest request) {
        log.info("Create payment request: {}", request);
        PaymentResponse payment= paymentService.create(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(payment);
    }

    @GetMapping
    public ResponseEntity<List<PaymentResponse>> getAll() {
        log.info("Get all payments request");

        List<PaymentResponse> response=paymentService.getAll();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse> getById(@PathVariable("id") Long id) {
        log.info("Get payment request with id: {}", id);
        PaymentResponse payment= paymentService.getById(id);
        return ResponseEntity.status(HttpStatus.OK).body(payment);
    }
}
