package org.example.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.exceptions.CandidateException;
import org.example.exceptions.ExamException;
import org.example.mapper.CandidateMapper;
import org.example.model.dto.CandidateResponse;
import org.example.model.dto.CreateCandidateRequest;
import org.example.model.entities.Candidate;
import org.example.model.entities.Exam;
import org.example.model.entities.Payment;
import org.example.repository.CandidateRepository;
import org.example.repository.ExamRepository;
import org.example.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Slf4j
@Service
public class CandidateService {

    private final CandidateRepository candidateRepository;
    private final ExamRepository  examRepository;
    private final CandidateMapper candidateMapper;
    private final PaymentRepository paymentRepository;

    @Transactional
    public CandidateResponse addCandidate(CreateCandidateRequest request) {
        if(candidateRepository.findByEmail(request.email()) != null){
            throw new CandidateException("Email already exists: "+request.email());
        }

        Exam exam=examRepository.findById(request.examId()).orElseThrow(()-> new ExamException("Exam not found"));

        Candidate candidate = candidateMapper.fromDtoToEntity(request); // map DTO -> entity
        candidate.setExam(exam); // legatura obligatorie

        Candidate savedCandidate= candidateRepository.save(candidate);
        log.info("Candidate {} saved successfully", savedCandidate.getName());

        return candidateMapper.fromEntityToDto(savedCandidate);
    }

    public List<CandidateResponse> findAllCandidates() {
        List<Candidate> candidates=candidateRepository.findAll();

        return candidates.stream().map(candidateMapper::fromEntityToDto).toList();
    }

    public CandidateResponse findById(Long id){
        Candidate candidate=candidateRepository.findById(id).orElseThrow(()-> new CandidateException("Candidate not found"));

        return candidateMapper.fromEntityToDto(candidate);
    }

    @Transactional
    public CandidateResponse updateName(Long id, String name){
        Candidate candidate=candidateRepository.findById(id).orElseThrow(()-> new CandidateException("Candidate not found"));

        candidate.setName(name);
        Candidate updatedCandidate=candidateRepository.save(candidate);
        log.info("Candidate {} updated name to {}", id, name);

        return candidateMapper.fromEntityToDto(updatedCandidate);
    }

    public List<CandidateResponse> findByExamId(Long id){
        List<Candidate> candidates=candidateRepository.findCandidatesByExamId(id);

        return  candidates.stream().map(candidateMapper::fromEntityToDto).toList();
    }

//    public List<String> getCandidateNamesForExam(Long examId) {
//        return candidateRepository.findCandidateNamesByExamId(examId);
//    }

    public List<CandidateResponse> findByExamName(String examName) {
        List<Candidate> candidates = candidateRepository.findCandidatesByExamName(examName);
        return candidates.stream().map(candidateMapper::fromEntityToDto).toList();
    }

    public List<CandidateResponse> findCandidatesWithPayment() {
        List<Payment> payments = paymentRepository.findAllByCandidateIsNotNull();

        return payments.stream()
                .map(p -> {
                    var c = p.getCandidate();
                    return new CandidateResponse(c.getId(), c.getName(), c.getEmail(), c.getExam().getName());
                })
                .collect(Collectors.toList());
    }





}
