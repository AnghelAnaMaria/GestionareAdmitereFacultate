package org.example.service;


import org.example.exceptions.PaymentException;
import org.example.mapper.PaymentMapper;
import org.example.model.dto.PaymentRequest;
import org.example.model.dto.PaymentResponse;
import org.example.model.entities.Candidate;
import org.example.model.entities.Payment;
import org.example.repository.CandidateRepository;
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
public class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private CandidateRepository  candidateRepository;

    @Mock
    private PaymentMapper paymentMapper;

    @InjectMocks
    private PaymentService paymentService; //injectam mock-urile in serviciul real

    @Test
    void whenPaymentExists_getById_returnsPaymentResponse() {
        // Arrange
        Candidate candidate = new Candidate();
        candidate.setId(1L);
        candidate.setName("Ana");
        candidate.setEmail("ana@mail.com");

        Payment payment = new Payment();
        payment.setId(1L);
        payment.setAmount(100.0);
        payment.setCandidate(candidate);

        PaymentResponse responseDto = new PaymentResponse();
        responseDto.setId(1L);
        responseDto.setAmount(100.0);
        responseDto.setCandidateName(candidate.getName());

        when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));

        when(paymentMapper.fromEntityToDto(payment)).thenReturn(responseDto);

        // Act
        PaymentResponse response = paymentService.getById(1L);

        // Assert
        assertNotNull(response);
        assertEquals(payment.getId(), response.getId());
        assertEquals(payment.getAmount(), response.getAmount());
        verify(paymentRepository).findById(1L);
    }

    @Test
    void whenPaymentDoesNotExist_getById_throwsException() {
        // Arrange
        when(paymentRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        PaymentException exception = assertThrows(PaymentException.class, () -> paymentService.getById(99L));
        assertEquals("Payment not found", exception.getMessage());
        verify(paymentRepository).findById(99L);
    }

    @Test
    void getAll_returnsListOfPaymentResponses() {
        // Arrange
        Candidate candidate1 = new Candidate();
        candidate1.setId(1L);
        candidate1.setName("Ana");
        candidate1.setEmail("ana@mail.com");

        Candidate candidate2 = new Candidate();
        candidate2.setId(2L);
        candidate2.setName("Ion");
        candidate2.setEmail("ion@mail.com");

        Payment payment1 = new Payment();
        payment1.setId(1L);
        payment1.setAmount(50.0);
        payment1.setCandidate(candidate1);

        Payment payment2 = new Payment();
        payment2.setId(2L);
        payment2.setAmount(75.0);
        payment2.setCandidate(candidate2);

        PaymentResponse response1 = new PaymentResponse();
        response1.setId(1L);
        response1.setAmount(50.0);

        PaymentResponse response2 = new PaymentResponse();
        response2.setId(2L);
        response2.setAmount(75.0);

        // Mock repository și mapper
        when(paymentRepository.findAll()).thenReturn(List.of(payment1, payment2));
        when(paymentMapper.fromEntityToDto(payment1)).thenReturn(response1);
        when(paymentMapper.fromEntityToDto(payment2)).thenReturn(response2);

        // Act
        List<PaymentResponse> results = paymentService.getAll();

        // Assert
        assertNotNull(results);
        assertEquals(2, results.size());
        assertEquals(50.0, results.get(0).getAmount());
        assertEquals(75.0, results.get(1).getAmount());
        verify(paymentRepository).findAll();
    }


    @Test
    void createPayment_savesAndReturnsPaymentResponse_candidateHasNoPayment() {
        // Arrange
        LocalDate paymentDate = LocalDate.of(2026, 1, 12);
        PaymentRequest request = new PaymentRequest(2000.0, paymentDate, 1L);

        Candidate candidate = new Candidate();
        candidate.setId(1L);
        candidate.setName("Ana");
        candidate.setEmail("ana@mail.com");

        Payment paymentEntity = new Payment();
        paymentEntity.setAmount(2000.0);
        paymentEntity.setPaymentDate(paymentDate);
        paymentEntity.setCandidate(candidate);

        Payment savedEntity = new Payment();
        savedEntity.setId(1L);
        savedEntity.setAmount(2000.0);
        savedEntity.setPaymentDate(paymentDate);
        savedEntity.setCandidate(candidate);

        PaymentResponse responseDto = new PaymentResponse();
        responseDto.setId(1L);
        responseDto.setAmount(2000.0);
        responseDto.setPaymentDate(paymentDate);

        // Mock behavior
        when(candidateRepository.findById(1L)).thenReturn(Optional.of(candidate));
        when(paymentRepository.findByCandidateId(1L)).thenReturn(Optional.empty());
        when(paymentMapper.fromDtoToEntity(request)).thenReturn(paymentEntity);
        when(paymentRepository.save(paymentEntity)).thenReturn(savedEntity);
        when(paymentMapper.fromEntityToDto(savedEntity)).thenReturn(responseDto);

        // Act
        PaymentResponse result = paymentService.create(request);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(2000.0, result.getAmount());
        assertEquals(paymentDate, result.getPaymentDate());

        verify(candidateRepository).findById(1L);
        verify(paymentRepository).findByCandidateId(1L);
        verify(paymentRepository).save(paymentEntity);
        verify(paymentMapper).fromEntityToDto(savedEntity);
    }

    @Test
    void createPayment_candidateAlreadyHasPayment_throwsException() {
        // Arrange
        LocalDate paymentDate = LocalDate.of(2026, 1, 12);
        PaymentRequest request = new PaymentRequest(2000.0, paymentDate, 1L);

        Candidate candidate = new Candidate();
        candidate.setId(1L);
        candidate.setName("Ana");
        candidate.setEmail("ana@mail.com");

        Payment existingPayment = new Payment();
        existingPayment.setId(99L);
        existingPayment.setCandidate(candidate);

        when(candidateRepository.findById(1L)).thenReturn(Optional.of(candidate));
        when(paymentRepository.findByCandidateId(1L)).thenReturn(Optional.of(existingPayment));

        // Act & Assert
        PaymentException ex = assertThrows(PaymentException.class, () -> paymentService.create(request));
        assertEquals("Candidate already has a payment", ex.getMessage());

        verify(candidateRepository).findById(1L);
        verify(paymentRepository).findByCandidateId(1L);
        verify(paymentRepository, never()).save(any(Payment.class));
    }












}
