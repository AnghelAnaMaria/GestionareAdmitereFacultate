package org.example.model.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.model.entities.ExamType;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExamDetailResponse {

    private Long id;

    private Integer durationMinutes;
    private ExamType examType;

    private String examName; //relatie cu Exam
}
