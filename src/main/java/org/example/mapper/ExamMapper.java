package org.example.mapper;


import org.example.model.dto.CreateExamRequest;
import org.example.model.dto.ExamResponse;
import org.example.model.entities.Candidate;
import org.example.model.entities.Exam;
import org.example.model.entities.ExamDetail;
import org.example.model.entities.ExamType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ExamMapper {

    public ExamResponse fromEntityToDto(Exam exam){
        ExamResponse dto = new ExamResponse();
        dto.setId(exam.getId());
        dto.setName(exam.getName());
        dto.setExamDate(exam.getExamDate());

        if(exam.getExamDetail() != null){
            dto.setDurationMinutes(exam.getExamDetail().getDurationMinutes());
            dto.setExamType(exam.getExamDetail().getExamType());
        }

        if(exam.getCandidates() !=null && !exam.getCandidates().isEmpty()){
            List<String> candidates = exam.getCandidates().stream().map(Candidate::getName).collect(Collectors.toList());

            dto.setCandidateNames(candidates);
        }
        return dto;
    }

    public Exam fromDtoToEntity(CreateExamRequest dto){
        Exam entity = new Exam();
        entity.setName(dto.name());
        entity.setExamDate(dto.examDate());

        ExamDetail detail = new ExamDetail();//Exam Detail creat odata cu Exam (si are si id)
        detail.setDurationMinutes(dto.durationMinutes() != null ? dto.durationMinutes() : 0);
        detail.setExamType(dto.examType() != null ? dto.examType() : ExamType.WRITTEN);

        detail.setExam(entity);// ExamDetail legat de Exam
        entity.setExamDetail(detail); //Exam legat de ExamDetail

        return entity;
    }
}
