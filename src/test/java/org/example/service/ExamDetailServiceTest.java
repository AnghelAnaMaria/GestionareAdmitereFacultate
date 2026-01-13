package org.example.service;


import org.example.exceptions.ExamException;
import org.example.mapper.ExamDetailMapper;
import org.example.model.dto.ExamDetailRequest;
import org.example.model.dto.ExamDetailResponse;
import org.example.model.entities.Exam;
import org.example.model.entities.ExamDetail;
import org.example.model.entities.ExamType;
import org.example.repository.ExamDetailRepository;
import org.example.repository.ExamRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExamDetailServiceTest {

    @Mock
    private ExamDetailRepository examDetailRepository;

    @Mock
    private ExamRepository examRepository;

    @Mock
    private ExamDetailMapper examDetailMapper;

    @InjectMocks
    private ExamDetailService examDetailService;

    @Test
    void updateExamDetail_whenExamNotFound_throwsExamException() {
        when(examRepository.findById(1L)).thenReturn(Optional.empty());

        ExamDetailRequest request = new ExamDetailRequest(120, ExamType.WRITTEN);

        assertThrows(ExamException.class, () -> examDetailService.updateExamDetail(1L, request));

        verify(examRepository, never()).save(any());
    }

    @Test
    void updateExamDetail_whenExamExistsAndDetailExists_updatesAndReturnsResponse() {
        Long examId = 1L;

        ExamDetailRequest request = new ExamDetailRequest(120, ExamType.WRITTEN);

        ExamDetail examDetail = new ExamDetail();
        examDetail.setDurationMinutes(90);
        examDetail.setExamType(ExamType.ORAL);

        Exam exam = Exam.builder().id(examId).name("Math").examDate(LocalDate.now()).examDetail(examDetail).build();

        examDetail.setExam(exam);

        ExamDetailResponse response = ExamDetailResponse.builder().id(10L).durationMinutes(120).examType(ExamType.WRITTEN).examName("Math").build();

        when(examRepository.findById(examId))
                .thenReturn(Optional.of(exam));

        doNothing().when(examDetailMapper)
                .updateEntityFromDto(request, examDetail);

        when(examRepository.save(exam))
                .thenReturn(exam);

        when(examDetailMapper.fromEntityToDto(examDetail))
                .thenReturn(response);

        ExamDetailResponse result = examDetailService.updateExamDetail(examId, request);

        assertNotNull(result);
        assertEquals(120, result.getDurationMinutes());
        assertEquals(ExamType.WRITTEN, result.getExamType());

        verify(examRepository).findById(examId);
        verify(examRepository).save(exam);
        verify(examDetailMapper).updateEntityFromDto(request, examDetail);
    }

    @Test
    void updateExamDetail_whenExamExistsAndDetailDoesNotExist_createsDetailAndReturnsResponse() {
        Long examId = 1L;

        ExamDetailRequest request = new ExamDetailRequest(180, ExamType.ORAL);

        Exam exam = Exam.builder().id(examId).name("Physics").examDate(LocalDate.now()).examDetail(null).build();

        ExamDetailResponse response = ExamDetailResponse.builder().durationMinutes(180).examType(ExamType.ORAL).examName("Physics").build();

        when(examRepository.findById(examId)).thenReturn(Optional.of(exam));

        doNothing().when(examDetailMapper).updateEntityFromDto(any(), any());

        when(examRepository.save(exam)).thenReturn(exam);

        when(examDetailMapper.fromEntityToDto(any(ExamDetail.class))).thenReturn(response);

        ExamDetailResponse result = examDetailService.updateExamDetail(examId, request);

        assertNotNull(result);
        assertEquals(180, result.getDurationMinutes());
        assertEquals(ExamType.ORAL, result.getExamType());

        assertNotNull(exam.getExamDetail());
        assertEquals(exam, exam.getExamDetail().getExam());

        verify(examRepository).save(exam);
    }



}
