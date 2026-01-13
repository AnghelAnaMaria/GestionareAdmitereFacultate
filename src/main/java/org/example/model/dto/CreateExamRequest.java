package org.example.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.example.model.entities.ExamType;

import java.time.LocalDate;

public record CreateExamRequest (@NotNull(message = "name cannot be null")
                                 @NotBlank(message = "name cannot be blank")
                                 String name,

                                 @NotNull(message = "exam date cannot be null")
                                 LocalDate examDate,

                                 Integer durationMinutes,
                                 ExamType examType){
}
