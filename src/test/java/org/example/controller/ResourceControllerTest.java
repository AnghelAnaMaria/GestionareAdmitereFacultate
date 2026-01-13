package org.example.controller;

import org.example.model.dto.ResourceRequest;
import org.example.model.dto.ResourceResponse;
import org.example.service.ResourceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ResourceControllerTest {

    @Mock
    private ResourceService resourceService;

    @InjectMocks
    private ResourceController resourceController;

    private ResourceRequest sampleRequest;
    private ResourceResponse sampleResponse;

    @BeforeEach
    void setUp() {
        sampleRequest = new ResourceRequest("http://example.com/resource", "Example resource", 1L);
        sampleResponse = ResourceResponse.builder().id(1L).url("http://example.com/resource").description("Example resource").build();
    }

    @Test
    void create_returnsCreatedResourceResponse() {
        // Arrange
        when(resourceService.create(sampleRequest)).thenReturn(sampleResponse);

        // Act
        ResponseEntity<ResourceResponse> result = resourceController.create(sampleRequest);

        // Assert
        assertEquals(201, result.getStatusCode().value());
        assertEquals(sampleResponse, result.getBody());
        verify(resourceService).create(sampleRequest);
    }

    @Test
    void getByExamDetailId_returnsResourceList() {
        // Arrange
        Long examDetailId = 1L;
        List<ResourceResponse> responseList = List.of(sampleResponse);
        when(resourceService.findByExamDetailId(examDetailId)).thenReturn(responseList);

        // Act
        ResponseEntity<List<ResourceResponse>> result = resourceController.getByExamDetailId(examDetailId);

        // Assert
        assertEquals(200, result.getStatusCode().value());
        assertEquals(responseList, result.getBody());
        verify(resourceService).findByExamDetailId(examDetailId);
    }
}
