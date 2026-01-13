package org.example.service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.exceptions.ExamException;
import org.example.mapper.ExamMapper;
import org.example.model.dto.CreateExamRequest;
import org.example.model.dto.ExamResponse;
import org.example.model.entities.Exam;
import org.example.model.entities.ExamDetail;
import org.example.model.entities.ExamType;
import org.example.repository.ExamDetailRepository;
import org.example.repository.ExamRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class ExamService {

    private final ExamRepository examRepository;
    private final ExamMapper examMapper;

    @Transactional
    public ExamResponse addExam(CreateExamRequest request) {

        Exam exam = examMapper.fromDtoToEntity(request);
        Exam savedExam = examRepository.save(exam);
        log.info("Exam {} was successfully saved in db.", savedExam);
        return examMapper.fromEntityToDto(savedExam);
    }

    public List<ExamResponse> findAllExams(){
        List<Exam> exams = examRepository.findAll();

        return exams.stream().map(examMapper::fromEntityToDto).toList();
    }

    public ExamResponse findById(Long id){
        Exam exam = examRepository.findById(id).orElseThrow(()-> new ExamException("Exam not found"));

        return examMapper.fromEntityToDto(exam);
    }

    @Transactional
    public ExamResponse updateDate(Long id, LocalDate newDate){
        Exam exam = examRepository.findById(id).orElseThrow(()-> new ExamException("Exam not found"));
        exam.setExamDate(newDate);

        Exam savedExam = examRepository.save(exam);
        log.info("Updated exam {} date to {}", id, newDate);
        return examMapper.fromEntityToDto(savedExam);
    }


    public List<ExamResponse> getCandidateCountPerExamDto() {
        List<Object[]> results = examRepository.countCandidatesPerExam();

        // mapare fiecare Object[] la un DTO
        return results.stream().map(obj -> {String examName = (String) obj[0];
                    Number countNum = (Number) obj[1]; // Number prinde Long, BigInteger, etc
                    int count = countNum != null ? countNum.intValue() : 0;
                    return ExamResponse.builder().name(examName).candidateCount(count).build();}).toList();
    }

    public List<ExamResponse> getExamsByTypeDto(ExamType examType) {
        List<Exam> exams = examRepository.findExamsByType(examType);
        return exams.stream().map(examMapper::fromEntityToDto).toList();
    }

    public ExamResponse getExamWithMostCandidatesDto() {
        Exam exam = examRepository.findExamWithMostCandidates();
        return examMapper.fromEntityToDto(exam);
    }

    public List<ExamResponse> getExams(Map<String, String> filters) {
        List<Exam> exams;

        if (filters != null && !filters.isEmpty()) {

            ExamType examType = ExamType.getValueByString(filters.get("examType"));

            if (examType != null) {
                exams = examRepository.findAllByExamDetail_ExamType(examType);
            } else {
                exams = examRepository.findAll();
            }

            log.debug("Returning {} exams filtered by {}", exams.size(), filters);
        } else {
            exams = examRepository.findAll();
            log.debug("Returning all exams");
        }

        return exams.stream().map(examMapper::fromEntityToDto).toList();
    }

    public List<ExamResponse> getExamsByFilters(ExamType examType, Integer durationMinutes, LocalDate examDate) {
        List<Exam> exams = examRepository.findByFilters(examType, durationMinutes, examDate);
        return exams.stream().map(examMapper::fromEntityToDto).toList();
    }

    public ExamResponse getExamWithMostResources() {
        Exam exam = examRepository.findExamWithMostResources();

        List<String> candidateNames = exam.getCandidates() != null ? exam.getCandidates().stream()
                .map(c -> c.getName()).toList() : List.of();

        return ExamResponse.builder()
                .id(exam.getId())
                .name(exam.getName())
                .examDate(exam.getExamDate())
                .durationMinutes(exam.getExamDetail().getDurationMinutes())
                .examType(exam.getExamDetail().getExamType())
                .candidateCount(exam.getCandidates() != null ? exam.getCandidates().size() : 0)
                .candidateNames(candidateNames)
                .build();
    }

}
