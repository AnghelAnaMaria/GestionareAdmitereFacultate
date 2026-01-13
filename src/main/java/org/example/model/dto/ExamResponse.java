package org.example.model.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.model.entities.ExamType;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExamResponse {

    private Long id;
    private String name;
    private LocalDate examDate;

    private Integer durationMinutes;
    private ExamType examType;

    //lista candidaților admisibili (pentru MVP)
    private List<String> candidateNames;

    private Integer candidateCount; //pt Query din exam repository
}
