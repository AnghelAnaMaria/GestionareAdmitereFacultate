package org.example.service;


import org.example.exceptions.ExamException;
import org.example.mapper.ExamMapper;
import org.example.model.dto.CreateExamRequest;
import org.example.model.dto.ExamResponse;
import org.example.model.entities.Exam;
import org.example.model.entities.ExamType;
import org.example.repository.ExamRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ExamServiceTest {

    @Mock
    private ExamRepository examRepository;

    @Mock
    private ExamMapper examMapper;

    @InjectMocks
    private ExamService examService;

    @Test
    void addExam_whenRequestIsValid_savesAndReturnsResponse() {
        CreateExamRequest request = new CreateExamRequest("Math Exam", LocalDate.now(), 120, ExamType.WRITTEN);

        Exam examEntity = Exam.builder().name("Math Exam").examDate(request.examDate()).build();

        Exam savedExam = Exam.builder().id(1L).name("Math Exam").examDate(request.examDate()).build();

        ExamResponse response = ExamResponse.builder().id(1L).name("Math Exam").examDate(request.examDate()).build();

        when(examMapper.fromDtoToEntity(request)).thenReturn(examEntity);
        when(examRepository.save(examEntity)).thenReturn(savedExam);
        when(examMapper.fromEntityToDto(savedExam)).thenReturn(response);

        ExamResponse result = examService.addExam(request);

        assertNotNull(result);
        assertEquals("Math Exam", result.getName());
        verify(examRepository).save(examEntity);
    }

    @Test
    void findAllExams_returnsMappedList() {
        Exam exam1 = Exam.builder().id(1L).name("Exam1").build();
        Exam exam2 = Exam.builder().id(2L).name("Exam2").build();

        when(examRepository.findAll()).thenReturn(List.of(exam1, exam2));

        ExamResponse resp1 = ExamResponse.builder().id(1L).name("Exam1").build();
        ExamResponse resp2 = ExamResponse.builder().id(2L).name("Exam2").build();

        when(examMapper.fromEntityToDto(exam1)).thenReturn(resp1);
        when(examMapper.fromEntityToDto(exam2)).thenReturn(resp2);

        List<ExamResponse> result = examService.findAllExams();

        assertEquals(2, result.size());
        assertTrue(result.contains(resp1));
        assertTrue(result.contains(resp2));
    }

    @Test
    void findById_whenExamExists_returnsMappedResponse() {
        Long examId = 1L;
        Exam exam = Exam.builder().id(examId).name("Physics").build();

        ExamResponse response = ExamResponse.builder().id(examId).name("Physics").build();

        when(examRepository.findById(examId)).thenReturn(Optional.of(exam));
        when(examMapper.fromEntityToDto(exam)).thenReturn(response);

        ExamResponse result = examService.findById(examId);

        assertEquals(response, result);
    }

    @Test
    void findById_whenExamNotFound_throwsExamException() {
        when(examRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ExamException.class, () -> examService.findById(1L));
    }

    @Test
    void updateDate_whenExamExists_updatesAndReturnsResponse() {
        Long examId = 1L;
        LocalDate newDate = LocalDate.now().plusDays(10);

        Exam exam = Exam.builder().id(examId).examDate(LocalDate.now()).build();

        Exam updatedExam = Exam.builder().id(examId).examDate(newDate).build();

        ExamResponse response = ExamResponse.builder().id(examId).examDate(newDate).build();

        when(examRepository.findById(examId)).thenReturn(Optional.of(exam));
        when(examRepository.save(exam)).thenReturn(updatedExam);
        when(examMapper.fromEntityToDto(updatedExam)).thenReturn(response);

        ExamResponse result = examService.updateDate(examId, newDate);

        assertEquals(newDate, result.getExamDate());
        verify(examRepository).save(exam);
    }

    @Test
    void updateDate_whenExamNotFound_throwsExamException() {
        when(examRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ExamException.class, () -> examService.updateDate(1L, LocalDate.now()));
    }

    @Test
    void getCandidateCountPerExamDto_returnsMappedCounts() {
        Object[] row1 = {"Math", 10L};
        Object[] row2 = {"Physics", 5L};

        when(examRepository.countCandidatesPerExam()).thenReturn(List.of(row1, row2));

        List<ExamResponse> result = examService.getCandidateCountPerExamDto();

        assertEquals(2, result.size());
        assertEquals("Math", result.get(0).getName());
        assertEquals(10, result.get(0).getCandidateCount());
    }

    @Test
    void getExamsByTypeDto_returnsMappedList() {
        ExamType type = ExamType.WRITTEN;

        Exam exam = Exam.builder().id(1L).name("Math").build();
        when(examRepository.findExamsByType(type)).thenReturn(List.of(exam));

        ExamResponse response = ExamResponse.builder().id(1L).name("Math").build();

        when(examMapper.fromEntityToDto(exam)).thenReturn(response);

        List<ExamResponse> result = examService.getExamsByTypeDto(type);

        assertEquals(1, result.size());
        assertEquals(response, result.get(0));
    }

    @Test
    void getExamWithMostCandidatesDto_returnsMappedResponse() {
        Exam exam = Exam.builder().id(1L).name("Top Exam").build();

        ExamResponse response = ExamResponse.builder().id(1L).name("Top Exam").build();

        when(examRepository.findExamWithMostCandidates()).thenReturn(exam);
        when(examMapper.fromEntityToDto(exam)).thenReturn(response);

        ExamResponse result = examService.getExamWithMostCandidatesDto();

        assertEquals(response, result);
    }

    @Test
    void getExamsByFilters_returnsMappedList() {
        // Arrange
        ExamType type = ExamType.ORAL;
        Integer durationMinutes = 120;
        LocalDate examDate = LocalDate.of(2026, 1, 13);

        Exam exam1 = Exam.builder().id(1L).name("Math Exam").examDate(examDate).build();
        Exam exam2 = Exam.builder().id(2L).name("Physics Exam").examDate(examDate).build();

        when(examRepository.findByFilters(type, durationMinutes, examDate)).thenReturn(List.of(exam1, exam2));

        ExamResponse resp1 = ExamResponse.builder().id(1L).name("Math Exam").examDate(examDate).build();
        ExamResponse resp2 = ExamResponse.builder().id(2L).name("Physics Exam").examDate(examDate).build();

        when(examMapper.fromEntityToDto(exam1)).thenReturn(resp1);
        when(examMapper.fromEntityToDto(exam2)).thenReturn(resp2);

        // Act
        List<ExamResponse> result = examService.getExamsByFilters(type, durationMinutes, examDate);

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(resp1));
        assertTrue(result.contains(resp2));

        verify(examRepository).findByFilters(type, durationMinutes, examDate);
    }

    @Test
    void getExamsByFilters_withNullFilters_returnsAllExams() {
        // Arrange
        ExamType type = null;
        Integer durationMinutes = null;
        LocalDate examDate = null;

        Exam exam1 = Exam.builder().id(1L).name("Math Exam").build();
        Exam exam2 = Exam.builder().id(2L).name("Physics Exam").build();

        when(examRepository.findByFilters(type, durationMinutes, examDate)).thenReturn(List.of(exam1, exam2));

        ExamResponse resp1 = ExamResponse.builder().id(1L).name("Math Exam").build();
        ExamResponse resp2 = ExamResponse.builder().id(2L).name("Physics Exam").build();

        when(examMapper.fromEntityToDto(exam1)).thenReturn(resp1);
        when(examMapper.fromEntityToDto(exam2)).thenReturn(resp2);

        // Act
        List<ExamResponse> result = examService.getExamsByFilters(type, durationMinutes, examDate);

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(resp1));
        assertTrue(result.contains(resp2));

        verify(examRepository).findByFilters(type, durationMinutes, examDate);
    }








}
