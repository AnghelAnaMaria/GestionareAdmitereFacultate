package org.example.controller;


import org.example.model.dto.CandidateResponse;
import org.example.model.dto.CreateCandidateRequest;
import org.example.service.CandidateService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CandidateControllerTest {

    @Mock
    private CandidateService candidateService;

    @InjectMocks
    private CandidateController candidateController;

    @Test
    void create_returnsCreatedCandidateResponse() {
        // Arrange
        CreateCandidateRequest request = new CreateCandidateRequest("Ana", "ana@mail.com", 1L);
        CandidateResponse response = new CandidateResponse(1L, "Ana", "ana@mail.com", "Physics");

        when(candidateService.addCandidate(request)).thenReturn(response);
        // Act
        ResponseEntity<CandidateResponse> result = candidateController.create(request);

        // Assert
        assertEquals(201, result.getStatusCode().value());
        assertEquals(response, result.getBody());

        verify(candidateService).addCandidate(request);
    }

    @Test
    void getAll_returnsListOfCandidates() {
        // Arrange
        CandidateResponse c1 = new CandidateResponse(1L, "Ana", "ana@mail.com", "Physics");
        CandidateResponse c2 = new CandidateResponse(2L, "Ion", "ion@mail.com", "Math");

        when(candidateService.findAllCandidates()).thenReturn(List.of(c1, c2));

        // Act
        ResponseEntity<List<CandidateResponse>> result = candidateController.getAll();

        // Assert
        assertEquals(200, result.getStatusCode().value());
        assertEquals(2, result.getBody().size());
        assertTrue(result.getBody().contains(c1));
        assertTrue(result.getBody().contains(c2));

        verify(candidateService).findAllCandidates();
    }

    @Test
    void getCandidateById_returnsCandidate() {
        // Arrange
        Long id = 1L;
        CandidateResponse response = new CandidateResponse(id, "Ana", "ana@mail.com", "Physics");

        when(candidateService.findById(id)).thenReturn(response);

        // Act
        ResponseEntity<CandidateResponse> result = candidateController.getCandidateById(id);

        // Assert
        assertEquals(200, result.getStatusCode().value());
        assertEquals(response, result.getBody());

        verify(candidateService).findById(id);
    }

    @Test
    void updateName_returnsUpdatedCandidate() {
        // Arrange
        Long id = 1L;
        String newName = "Ana Maria";
        CandidateResponse response = new CandidateResponse(id, newName, "ana@mail.com", "Physics");

        when(candidateService.updateName(id, newName)).thenReturn(response);

        // Act
        ResponseEntity<CandidateResponse> result = candidateController.updateName(id, newName);

        // Assert
        assertEquals(200, result.getStatusCode().value());
        assertEquals(response, result.getBody());

        verify(candidateService).updateName(id, newName);
    }

    @Test
    void getCandidatesByExam_returnsList() {
        // Arrange
        Long examId = 1L;
        CandidateResponse c1 = new CandidateResponse(1L, "Ana", "ana@mail.com", "Physics");
        CandidateResponse c2 = new CandidateResponse(2L, "Ion", "ion@mail.com", "Physics");

        when(candidateService.findByExamId(examId)).thenReturn(List.of(c1, c2));

        // Act
        ResponseEntity<List<CandidateResponse>> result = candidateController.getCandidatesByExam(examId);

        // Assert
        assertEquals(200, result.getStatusCode().value());
        assertEquals(2, result.getBody().size());
        assertTrue(result.getBody().contains(c1));
        assertTrue(result.getBody().contains(c2));

        verify(candidateService).findByExamId(examId);
    }

    @Test
    void getCandidatesByExamName_returnsList() {
        // Arrange
        String examName = "Physics";
        CandidateResponse c1 = new CandidateResponse(1L, "Ana", "ana@mail.com", examName);
        CandidateResponse c2 = new CandidateResponse(2L, "Ion", "ion@mail.com", examName);

        when(candidateService.findByExamName(examName)).thenReturn(List.of(c1, c2));

        // Act
        ResponseEntity<List<CandidateResponse>> result = candidateController.getCandidatesByExamName(examName);

        // Assert
        assertEquals(200, result.getStatusCode().value());
        assertEquals(2, result.getBody().size());
        assertTrue(result.getBody().contains(c1));
        assertTrue(result.getBody().contains(c2));

        verify(candidateService).findByExamName(examName);
    }
}









