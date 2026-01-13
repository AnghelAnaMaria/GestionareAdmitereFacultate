package org.example.service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.exceptions.CandidateException;
import org.example.exceptions.PaymentException;
import org.example.mapper.PaymentMapper;
import org.example.model.dto.PaymentRequest;
import org.example.model.dto.PaymentResponse;
import org.example.model.entities.Candidate;
import org.example.model.entities.Payment;
import org.example.repository.CandidateRepository;
import org.example.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final CandidateRepository candidateRepository;

    @Transactional
    public PaymentResponse create(PaymentRequest paymentRequest) {
        Candidate candidate = candidateRepository.findById(paymentRequest.candidateId()).orElseThrow(()-> new CandidateException("Candidate not found"));
        if (paymentRepository.findByCandidateId(candidate.getId()).isPresent()) {
            throw new PaymentException("Candidate already has a payment");
        }

        Payment payment = paymentMapper.fromDtoToEntity(paymentRequest);
        payment.setCandidate(candidate);//adaug Candidate
        Payment saved = paymentRepository.save(payment);
        return paymentMapper.fromEntityToDto(saved);
    }

    public List<PaymentResponse> getAll() {
        return paymentRepository.findAll().stream().map(paymentMapper::fromEntityToDto).toList();
    }

    public PaymentResponse getById(Long id) {
        Payment payment = paymentRepository.findById(id).orElseThrow(() -> new PaymentException("Payment not found"));
        return paymentMapper.fromEntityToDto(payment);
    }
}
