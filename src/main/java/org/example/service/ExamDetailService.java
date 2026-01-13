package org.example.service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.exceptions.ExamException;
import org.example.mapper.ExamDetailMapper;
import org.example.model.dto.ExamDetailRequest;
import org.example.model.dto.ExamDetailResponse;
import org.example.model.entities.Exam;
import org.example.model.entities.ExamDetail;
import org.example.repository.ExamDetailRepository;
import org.example.repository.ExamRepository;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ExamDetailService {

    private final ExamDetailRepository examDetailRepository;
    private final ExamRepository examRepository;
    private final ExamDetailMapper examDetailMapper;

    @Transactional
    public ExamDetailResponse updateExamDetail(Long examId, ExamDetailRequest examDetailRequest) {
        Exam exam = examRepository.findById(examId).orElseThrow(()-> new ExamException("Exam Not Found"));

        ExamDetail examDetail = exam.getExamDetail();
        if(examDetail == null){
            examDetail = new ExamDetail();
            examDetail.setExam(exam);
            exam.setExamDetail(examDetail);
        }

        examDetailMapper.updateEntityFromDto(examDetailRequest, examDetail);

        // salvez doar Exam → ExamDetail e salvat automat
        Exam savedExam = examRepository.save(exam);

        return examDetailMapper.fromEntityToDto(savedExam.getExamDetail());
    }


}
