package org.example.controller;



import org.example.model.dto.CreateExamRequest;
import org.example.model.dto.ExamDetailRequest;
import org.example.model.dto.ExamDetailResponse;
import org.example.model.dto.ExamResponse;
import org.example.model.entities.ExamType;
import org.example.service.ExamDetailService;
import org.example.service.ExamService;
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
public class ExamenControllerTest {

    @Mock
    private ExamService examService;

    @Mock
    private ExamDetailService examDetailService;

    @InjectMocks
    private ExamenController examenController;

    private CreateExamRequest sampleCreateRequest;
    private ExamResponse sampleExamResponse;
    private ExamDetailRequest sampleExamDetailRequest;
    private ExamDetailResponse sampleExamDetailResponse;

    @BeforeEach
    void setUp() {
        sampleCreateRequest = new CreateExamRequest("Math Exam", LocalDate.of(2026, 1, 13), 120, ExamType.WRITTEN);
        sampleExamResponse = ExamResponse.builder().id(1L).name("Math Exam").examDate(LocalDate.of(2026, 1, 13)).durationMinutes(120).examType(ExamType.WRITTEN).candidateNames(List.of("Ana", "Ion")).candidateCount(2).build();

        sampleExamDetailRequest = new ExamDetailRequest(90, ExamType.ORAL);
        sampleExamDetailResponse = new ExamDetailResponse(1L, 90, ExamType.ORAL, "Math Exam");
    }

    @Test
    void create_returnsCreatedExamResponse() {
        when(examService.addExam(sampleCreateRequest)).thenReturn(sampleExamResponse);

        ResponseEntity<ExamResponse> result = examenController.create(sampleCreateRequest);

        assertEquals(201, result.getStatusCode().value());
        assertEquals(sampleExamResponse, result.getBody());
        verify(examService).addExam(sampleCreateRequest);
    }

    @Test
    void getExams_returnsFilteredList() {
        ExamType typeFilter = ExamType.WRITTEN;
        Integer durationFilter = 120;
        LocalDate dateFilter = LocalDate.of(2026, 1, 13);

        when(examService.getExamsByFilters(typeFilter, durationFilter, dateFilter)).thenReturn(List.of(sampleExamResponse));

        ResponseEntity<List<ExamResponse>> result = examenController.getExams(typeFilter, durationFilter, dateFilter);

        assertEquals(200, result.getStatusCode().value());
        assertEquals(1, result.getBody().size());
        assertEquals(sampleExamResponse, result.getBody().get(0));
        verify(examService).getExamsByFilters(typeFilter, durationFilter, dateFilter);
    }

    @Test
    void getById_returnsExam() {
        Long id = 1L;
        when(examService.findById(id)).thenReturn(sampleExamResponse);

        ResponseEntity<ExamResponse> result = examenController.getById(id);

        assertEquals(200, result.getStatusCode().value());
        assertEquals(sampleExamResponse, result.getBody());
        verify(examService).findById(id);
    }

    @Test
    void updateDate_returnsUpdatedExam() {
        Long id = 1L;
        LocalDate newDate = LocalDate.of(2026, 1, 20);
        ExamResponse updatedResponse = ExamResponse.builder().id(1L).name("Math Exam").examDate(newDate).durationMinutes(120).examType(ExamType.WRITTEN).candidateNames(List.of("Ana", "Ion")).candidateCount(2).build();

        when(examService.updateDate(id, newDate)).thenReturn(updatedResponse);

        ResponseEntity<ExamResponse> result = examenController.updateDate(id, newDate);

        assertEquals(200, result.getStatusCode().value());
        assertEquals(updatedResponse, result.getBody());
        verify(examService).updateDate(id, newDate);
    }

    @Test
    void updateExamDetails_returnsUpdatedDetails() {
        Long examId = 1L;
        when(examDetailService.updateExamDetail(examId, sampleExamDetailRequest)).thenReturn(sampleExamDetailResponse);

        ResponseEntity<ExamDetailResponse> result = examenController.updateExamDetails(examId, sampleExamDetailRequest);

        assertEquals(200, result.getStatusCode().value());
        assertEquals(sampleExamDetailResponse, result.getBody());
        verify(examDetailService).updateExamDetail(examId, sampleExamDetailRequest);
    }

    @Test
    void getCandidateCountPerExam_returnsList() {
        when(examService.getCandidateCountPerExamDto()).thenReturn(List.of(sampleExamResponse));

        ResponseEntity<List<ExamResponse>> result = examenController.getCandidateCountPerExam();

        assertEquals(200, result.getStatusCode().value());
        assertEquals(1, result.getBody().size());
        assertEquals(sampleExamResponse, result.getBody().get(0));
        verify(examService).getCandidateCountPerExamDto();
    }

    @Test
    void getExamsByType_returnsList() {
        ExamType type = ExamType.WRITTEN;
        when(examService.getExamsByTypeDto(type)).thenReturn(List.of(sampleExamResponse));

        ResponseEntity<List<ExamResponse>> result = examenController.getExamsByType(type);

        assertEquals(200, result.getStatusCode().value());
        assertEquals(1, result.getBody().size());
        assertEquals(sampleExamResponse, result.getBody().get(0));
        verify(examService).getExamsByTypeDto(type);
    }

    @Test
    void getExamWithMostCandidates_returnsExam() {
        when(examService.getExamWithMostCandidatesDto()).thenReturn(sampleExamResponse);

        ResponseEntity<ExamResponse> result = examenController.getExamWithMostCandidates();

        assertEquals(200, result.getStatusCode().value());
        assertEquals(sampleExamResponse, result.getBody());
        verify(examService).getExamWithMostCandidatesDto();
    }




}
