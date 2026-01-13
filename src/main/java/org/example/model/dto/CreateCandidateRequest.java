package org.example.model.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public record CreateCandidateRequest(@NotNull(message = "name cannot be null")
                                     @NotBlank(message = "name cannot be blank")
                                     String name,

                                     @NotNull(message = "mail cannot be null")
                                     @NotBlank(message = "mail cannot be blank")
                                     @Email(message = "mail mast be valid")
                                     String email,

                                     @NotNull(message = "exam id cannot be null")
                                     Long examId) {

}
