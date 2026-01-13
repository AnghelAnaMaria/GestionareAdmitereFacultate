package org.example.service;


import org.example.exceptions.CandidateException;
import org.example.exceptions.ExamException;
import org.example.mapper.CandidateMapper;
import org.example.model.dto.CandidateResponse;
import org.example.model.dto.CreateCandidateRequest;
import org.example.model.entities.Candidate;
import org.example.model.entities.Exam;
import org.example.repository.CandidateRepository;
import org.example.repository.ExamRepository;
import org.example.repository.PaymentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CandidateServiceTest {

    @Mock
    private CandidateRepository candidateRepository;

    @Mock
    private ExamRepository examRepository;

    @Mock
    private CandidateMapper candidateMapper;

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private CandidateService candidateService;

    @Test
    void addCandidate_whenValidRequest_savesAndReturnsResponse() {
        Long examId = 1L;

        CreateCandidateRequest request = new CreateCandidateRequest("Ana", "ana@mail.com", examId);
        when(candidateRepository.findByEmail(request.email())).thenReturn(null);
        Exam exam = Exam.builder().id(examId).name("Math").build();
        when(examRepository.findById(examId)).thenReturn(Optional.of(exam));
        Candidate candidate = Candidate.builder().name("Ana").email("ana@mail.com").build();
        when(candidateMapper.fromDtoToEntity(request)).thenReturn(candidate);
        when(candidateRepository.save(candidate)).thenReturn(candidate);
        CandidateResponse response = new CandidateResponse(1L, "Ana", "ana@mail.com", "Math");
        when(candidateMapper.fromEntityToDto(candidate)).thenReturn(response);


        CandidateResponse result = candidateService.addCandidate(request);

        assertNotNull(result);
        assertEquals("Ana", result.getName());
        assertEquals("ana@mail.com", result.getEmail());
        assertEquals("Math", result.getExamName());

        assertEquals(exam, candidate.getExam());
        verify(candidateRepository).save(candidate);
    }

    @Test
    void addCandidate_whenEmailAlreadyExists_throwsCandidateException() {
        CreateCandidateRequest request = new CreateCandidateRequest("Ana", "ana@mail.com", 1L);

        when(candidateRepository.findByEmail(request.email())).thenReturn(new Candidate());

        assertThrows(CandidateException.class, () -> candidateService.addCandidate(request));

        verify(examRepository, never()).findById(any());
        verify(candidateRepository, never()).save(any());
    }

    @Test
    void addCandidate_whenExamNotFound_throwsExamException() {
        CreateCandidateRequest request = new CreateCandidateRequest("Ana", "ana@mail.com", 1L);

        when(candidateRepository.findByEmail(request.email())).thenReturn(null);

        when(examRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ExamException.class, () -> candidateService.addCandidate(request));

        verify(candidateRepository, never()).save(any());
    }

    @Test
    void findById_whenCandidateExists_returnsResponse() {
        Candidate candidate = Candidate.builder().id(1L).name("Ana").email("ana@mail.com").build();

        when(candidateRepository.findById(1L)).thenReturn(Optional.of(candidate));

        CandidateResponse response = new CandidateResponse(1L, "Ana", "ana@mail.com", "Math");

        when(candidateMapper.fromEntityToDto(candidate)).thenReturn(response);

        CandidateResponse result = candidateService.findById(1L);

        assertEquals(response, result);
    }

    @Test
    void updateName_whenCandidateExists_updatesAndReturnsResponse() {
        Candidate candidate = Candidate.builder().id(1L).name("Old").build();

        when(candidateRepository.findById(1L)).thenReturn(Optional.of(candidate));
        when(candidateRepository.save(candidate)).thenReturn(candidate);

        CandidateResponse response = new CandidateResponse(1L, "New", null, null);
        when(candidateMapper.fromEntityToDto(candidate)).thenReturn(response);

        CandidateResponse result = candidateService.updateName(1L, "New");

        assertEquals("New", candidate.getName());
        assertEquals("New", result.getName());
    }







}
