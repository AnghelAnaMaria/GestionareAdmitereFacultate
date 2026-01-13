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
public class ResourceResponse {

    private Long id;
    private String url;
    private String description;

    private ExamType type;

}
