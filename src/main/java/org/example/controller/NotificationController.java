package org.example.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.model.dto.NotificationRequest;
import org.example.model.dto.NotificationResponse;
import org.example.service.NotificationService;
import org.example.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping
    public ResponseEntity<NotificationResponse> create(@Valid @RequestBody NotificationRequest request) {
        log.info("Received request to create a new notification");
        NotificationResponse res= notificationService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @GetMapping("/payment/{paymentId}")
    public ResponseEntity<List<NotificationResponse>> getByPaymentId(@PathVariable("paymentId") Long paymentId){
        log.info("Received request to get all notifications by payment id: "+paymentId);
        List<NotificationResponse> res= notificationService.getByPaymentId(paymentId);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificationResponse> getById(@PathVariable("id") Long id){
        log.info("Received request to get notification by id: "+id);
        NotificationResponse notificationResponse=notificationService.getById(id);
        return ResponseEntity.status(HttpStatus.OK).body(notificationResponse);
    }

    @GetMapping("/high-payments")
    public ResponseEntity<List<String>> getHighPaymentMessages(@RequestParam Double minAmount) {
        List<String> messages = notificationService.getMessagesForHighPayments(minAmount);
        return ResponseEntity.ok(messages);
    }

}
