package org.example.service;

import org.example.exceptions.ExamDetailException;
import org.example.mapper.ResourceMapper;
import org.example.model.dto.ResourceRequest;
import org.example.model.dto.ResourceResponse;
import org.example.model.entities.ExamDetail;
import org.example.model.entities.ExamType;
import org.example.model.entities.Resource;
import org.example.repository.ExamDetailRepository;
import org.example.repository.ResourceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ResourceServiceTest {

    @Mock
    private ResourceRepository resourceRepository;

    @Mock
    private ResourceMapper resourceMapper;

    @Mock
    private ExamDetailRepository examDetailRepository;

    @InjectMocks
    private ResourceService resourceService;

    @Test
    void create_whenExamDetailExists_savesAndReturnsResponse() {
        // Arrange
        ResourceRequest request = new ResourceRequest("http://res.com", "pdf", 1L);

        ExamDetail examDetail = new ExamDetail();
        examDetail.setId(1L);

        Resource resourceEntity = Resource.builder().url(request.url()).description(request.description()).examDetail(examDetail).build();

        Resource savedResource = Resource.builder().id(100L).url(request.url()).description(request.description()).examDetail(examDetail).build();

        ResourceResponse response = new ResourceResponse(100L, request.url(), request.description(), ExamType.WRITTEN
        );

        when(examDetailRepository.findById(1L)).thenReturn(Optional.of(examDetail));

        when(resourceMapper.fromDtoToEntity(request, examDetail)).thenReturn(resourceEntity);

        when(resourceRepository.save(resourceEntity)).thenReturn(savedResource);

        when(resourceMapper.fromEntityoDto(savedResource)).thenReturn(response);

        // Act
        ResourceResponse result = resourceService.create(request);

        // Assert
        assertNotNull(result);
        assertEquals(100L, result.getId());
        assertEquals(request.url(), result.getUrl());

        verify(examDetailRepository).findById(1L);
        verify(resourceRepository).save(resourceEntity);
    }

    @Test
    void create_whenExamDetailNotFound_throwsException() {
        // Arrange
        ResourceRequest request = new ResourceRequest("http://res.com", "pdf", 1L);

        when(examDetailRepository.findById(1L)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(ExamDetailException.class, () -> resourceService.create(request));

        verify(resourceRepository, never()).save(any());
        verify(resourceMapper, never()).fromDtoToEntity(any(), any());
    }

    @Test
    void findByExamDetailId_returnsMappedList() {
        // Arrange
        Long examDetailId = 1L;

        Resource r1 = Resource.builder().id(1L).url("u1").build();
        Resource r2 = Resource.builder().id(2L).url("u2").build();

        when(resourceRepository.findByExamDetailId(examDetailId)).thenReturn(List.of(r1, r2));

        ResourceResponse resp1 = new ResourceResponse(1L, "u1", null, ExamType.ORAL);
        ResourceResponse resp2 = new ResourceResponse(2L, "u2", null, ExamType.ORAL);

        when(resourceMapper.fromEntityoDto(r1)).thenReturn(resp1);
        when(resourceMapper.fromEntityoDto(r2)).thenReturn(resp2);

        // Act
        List<ResourceResponse> result = resourceService.findByExamDetailId(examDetailId);

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(resp1));
        assertTrue(result.contains(resp2));

        verify(resourceRepository).findByExamDetailId(examDetailId);
    }
}






