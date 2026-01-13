package org.example.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.model.dto.ResourceRequest;
import org.example.model.dto.ResourceResponse;
import org.example.model.entities.Resource;
import org.example.service.ResourceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/resources")
public class ResourceController {

    private final ResourceService resourceService;

    @PostMapping
    public ResponseEntity<ResourceResponse> create(@Valid @RequestBody ResourceRequest request){
        ResourceResponse res= resourceService.create(request);

        log.info("Create resource request {}", request);

        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @GetMapping("/exam_detail/{id}")
    public ResponseEntity<List<ResourceResponse>> getByExamDetailId(@PathVariable("id") Long id){
        var response=resourceService.findByExamDetailId(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


}
