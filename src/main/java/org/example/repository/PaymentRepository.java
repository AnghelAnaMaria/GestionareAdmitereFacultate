package org.example.repository;

import org.example.model.entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByCandidateId(Long candidateId);

    List<Payment> findAllByCandidateIsNotNull();
}
