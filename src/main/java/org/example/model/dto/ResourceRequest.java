package org.example.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ResourceRequest (@NotBlank(message = "string cannot be null") String url,
                               String description,

                               @NotNull(message = "exam detail id should be grater than 1") Long examDetailId) {
}
