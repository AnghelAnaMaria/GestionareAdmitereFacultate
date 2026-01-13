package org.example.model.dto;

import jakarta.validation.constraints.NotNull;
import org.example.model.entities.ExamType;


//pt update
public record ExamDetailRequest(@NotNull(message = "duration cannot be null") Integer durationMinutes,
                                @NotNull(message = "exam type cannot be null") ExamType examType){
}
