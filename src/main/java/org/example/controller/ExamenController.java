package org.example.controller;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.model.dto.CreateExamRequest;
import org.example.model.dto.ExamDetailRequest;
import org.example.model.dto.ExamDetailResponse;
import org.example.model.dto.ExamResponse;
import org.example.model.entities.ExamType;
import org.example.service.ExamDetailService;
import org.example.service.ExamService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/exams")
public class ExamenController {

    private final ExamService examService;
    private final ExamDetailService examDetailService;

    @PostMapping
    public ResponseEntity<ExamResponse> create(@Valid @RequestBody CreateExamRequest request){
        log.info("Create exam request: {}", request);
        ExamResponse examResponse= examService.addExam(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(examResponse);
    }

//    @GetMapping
//    public ResponseEntity<List<ExamResponse>> getAll(){
//        log.info("Get all exams request");
//
//        List<ExamResponse> response=examService.findAllExams();
//        return ResponseEntity.status(HttpStatus.OK).body(response);
//    }
    @GetMapping
    public ResponseEntity<List<ExamResponse>> getExams(@RequestParam(required = false) ExamType examType,
                                                       @RequestParam(required = false) Integer durationMinutes,
                                                       @RequestParam(required = false) LocalDate examDate) {

        List<ExamResponse> response = examService.getExamsByFilters(examType, durationMinutes, examDate);
        return ResponseEntity.ok(response);
    }



    @GetMapping("/{id}")
    public ResponseEntity<ExamResponse> getById(@PathVariable("id") Long id){
        log.info("Get exam by id request: {}", id);
        ExamResponse response=examService.findById(id);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ExamResponse> updateDate(@PathVariable("id") Long id,
                                                   @NotNull @RequestParam("date") LocalDate date){
        log.info("Patch exam by id {} with new date {}", id, date);
        ExamResponse response= examService.updateDate(id, date);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    //exam detail service
    @PatchMapping("/{examId}/details")
    public ResponseEntity<ExamDetailResponse> updateExamDetails(@PathVariable("examId") Long id, @Valid @RequestBody ExamDetailRequest request){
        ExamDetailResponse response= examDetailService.updateExamDetail(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @GetMapping("/candidate-count")
    public ResponseEntity<List<ExamResponse>> getCandidateCountPerExam() {
        List<ExamResponse> result = examService.getCandidateCountPerExamDto();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/by-type")
    public ResponseEntity<List<ExamResponse>> getExamsByType(@RequestParam ExamType examType) {
        List<ExamResponse> exams = examService.getExamsByTypeDto(examType);
        return ResponseEntity.ok(exams);
    }

    @GetMapping("/most-candidates")
    public ResponseEntity<ExamResponse> getExamWithMostCandidates() {
        ExamResponse exam = examService.getExamWithMostCandidatesDto();
        return ResponseEntity.ok(exam);
    }

    @GetMapping("/most-resources")
    public ResponseEntity<ExamResponse> getExamWithMostResources() {
        ExamResponse exam = examService.getExamWithMostResources();
        return ResponseEntity.ok(exam);
    }




}
