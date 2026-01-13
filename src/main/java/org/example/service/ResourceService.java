package org.example.service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.exceptions.ExamDetailException;
import org.example.mapper.ResourceMapper;
import org.example.model.dto.ResourceRequest;
import org.example.model.dto.ResourceResponse;
import org.example.model.entities.ExamDetail;
import org.example.model.entities.Resource;
import org.example.repository.ExamDetailRepository;
import org.example.repository.ResourceRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResourceService {

    private final ResourceRepository  resourceRepository;
    private final ResourceMapper resourceMapper;
    private final ExamDetailRepository examDetailRepository;

    @Transactional
    public ResourceResponse create(ResourceRequest resourceRequest){
        ExamDetail examDetail = examDetailRepository.findById(resourceRequest.examDetailId()).orElseThrow(()-> new ExamDetailException("Exam Detail not found"));

        Resource resource= resourceMapper.fromDtoToEntity(resourceRequest, examDetail);

        Resource savedResource = resourceRepository.save(resource);

        log.info("Resource {} saved for examDetail {}", savedResource.getId(), examDetail.getId());

        return resourceMapper.fromEntityoDto(savedResource);
    }

    public List<ResourceResponse> findByExamDetailId(Long examDetailId){
        List<Resource> resources = resourceRepository.findByExamDetailId(examDetailId);
        log.info("Resources found for ExamDetail {}: {}", examDetailId, resources.size());
        resources.forEach(r -> log.info("Resource {}: {}", r.getId(), r.getUrl()));

        return resources.stream()
                .map(resourceMapper::fromEntityoDto)
                .toList();
    }
}
