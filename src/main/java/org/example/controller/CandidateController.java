package org.example.controller;


import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.model.dto.CandidateResponse;
import org.example.model.dto.CreateCandidateRequest;
import org.example.service.CandidateService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/candidates")
public class CandidateController {

    private final CandidateService candidateService;

    @PostMapping
    public ResponseEntity<CandidateResponse> create(@Valid @RequestBody CreateCandidateRequest request){
        log.info("Create candidate request: {}", request);
        CandidateResponse response = candidateService.addCandidate(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<CandidateResponse>> getAll(){
        log.info("Get all candidates");
        List<CandidateResponse> candidateResponses= candidateService.findAllCandidates();

        return ResponseEntity.status(HttpStatus.OK).body(candidateResponses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CandidateResponse> getCandidateById(@PathVariable("id") Long id){
        log.info("Get candidate by id request: {}", id);
        CandidateResponse candidateResponse= candidateService.findById(id);

        return ResponseEntity.status(HttpStatus.OK).body(candidateResponse);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CandidateResponse> updateName(@PathVariable("id") Long id,
                                                        @NotNull @RequestParam("name") String name){
        log.info("Patch candidate by id {} with name {}", id, name);
        CandidateResponse response= candidateService.updateName(id, name);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/exam/{examId}")
    public ResponseEntity<List<CandidateResponse>> getCandidatesByExam(@PathVariable("examId") Long examId){
        log.info("Get candidates by exam id request: {}", examId);
        List<CandidateResponse> candidateResponses= candidateService.findByExamId(examId);

        return ResponseEntity.status(HttpStatus.OK).body(candidateResponses);
    }

    @GetMapping("/by-exam-name")
    public ResponseEntity<List<CandidateResponse>> getCandidatesByExamName(@RequestParam("examName") @Parameter(description = "Name of the exam to filter candidates") String examName) {
        log.info("Get candidates by exam name request: {}", examName);
        List<CandidateResponse> candidateResponses = candidateService.findByExamName(examName);

        return ResponseEntity.status(HttpStatus.OK).body(candidateResponses);
    }

    @GetMapping("/with-payment")
    public ResponseEntity<List<CandidateResponse>> getCandidatesWithPayment() {
        List<CandidateResponse> result = candidateService.findCandidatesWithPayment();
        return ResponseEntity.ok(result);
    }

}
