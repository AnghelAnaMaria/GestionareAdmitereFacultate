package org.example.controller;


import org.example.model.dto.PaymentRequest;
import org.example.model.dto.PaymentResponse;
import org.example.service.PaymentService;
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
public class PaymentControllerTest {

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private PaymentController paymentController;

    private PaymentRequest sampleRequest;
    private PaymentResponse sampleResponse;

    @BeforeEach
    void setUp() {
        sampleRequest = new PaymentRequest( 100.0, LocalDate.now(),1L);
        sampleResponse = PaymentResponse.builder().id(1L).amount(100.0).paymentDate(LocalDate.now()).candidateName("Ana").notifications(List.of()).build();
    }

    @Test
    void create_returnsCreatedPaymentResponse() {
        when(paymentService.create(sampleRequest)).thenReturn(sampleResponse);

        ResponseEntity<PaymentResponse> result = paymentController.create(sampleRequest);

        assertEquals(201, result.getStatusCode().value());
        assertEquals(sampleResponse, result.getBody());
        verify(paymentService).create(sampleRequest);
    }

    @Test
    void getAll_returnsListOfPayments() {
        when(paymentService.getAll()).thenReturn(List.of(sampleResponse));

        ResponseEntity<List<PaymentResponse>> result = paymentController.getAll();

        assertEquals(200, result.getStatusCode().value());
        assertEquals(1, result.getBody().size());
        assertTrue(result.getBody().contains(sampleResponse));
        verify(paymentService).getAll();
    }

    @Test
    void getById_returnsPaymentResponse() {
        when(paymentService.getById(1L)).thenReturn(sampleResponse);

        ResponseEntity<PaymentResponse> result = paymentController.getById(1L);

        assertEquals(200, result.getStatusCode().value());
        assertEquals(sampleResponse, result.getBody());
        verify(paymentService).getById(1L);
    }

}
