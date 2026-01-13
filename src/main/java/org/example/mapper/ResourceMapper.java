package org.example.mapper;

import org.example.model.dto.ResourceRequest;
import org.example.model.dto.ResourceResponse;
import org.example.model.entities.ExamDetail;
import org.example.model.entities.Resource;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ResourceMapper {

    public ResourceResponse fromEntityoDto(Resource resource){
        ResourceResponse res = new ResourceResponse();
        res.setId(resource.getId());
        res.setDescription(resource.getDescription());
        res.setUrl(resource.getUrl());
//        res.setType(resource.getExamDetail() != null ? resource.getExamDetail().getExamType() : null);
        res.setType(Optional.ofNullable(resource.getExamDetail()).map(ExamDetail::getExamType).orElse(null));

        return res;
    }

    public Resource fromDtoToEntity(ResourceRequest dto, ExamDetail examDetail){
        return Resource.builder()
                .url(dto.url())
                .description(dto.description())
                .examDetail(examDetail)
                .build();
    }
}
