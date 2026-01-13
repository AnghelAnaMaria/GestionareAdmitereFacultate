package org.example.mapper;

import org.example.model.dto.ExamDetailRequest;
import org.example.model.dto.ExamDetailResponse;
import org.example.model.entities.ExamDetail;
import org.springframework.stereotype.Component;

@Component
public class ExamDetailMapper {

    public ExamDetailResponse fromEntityToDto(ExamDetail entity){
        ExamDetailResponse dto=new ExamDetailResponse();
        dto.setId(entity.getId());
        dto.setDurationMinutes(entity.getDurationMinutes());
        dto.setExamType(entity.getExamType());

        if(entity.getExam() != null){
            dto.setExamName(entity.getExam().getName());
        }
        return dto;
    }

//    public ExamDetail fromDtoToEntity(ExamDetailRequest request){
//
//    }

    public void updateEntityFromDto(ExamDetailRequest request, ExamDetail entity){
        entity.setExamType(request.examType());
        entity.setDurationMinutes(request.durationMinutes());
    }
}
