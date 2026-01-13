package org.example.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CandidateException.class)
    public ResponseEntity<?> handleCandidateException(CandidateException e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(ExamException.class)
    public ResponseEntity<?> handleExamException(ExamException e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(ExamDetailException.class)
    public ResponseEntity<?> handleExamDetailException(ExamDetailException e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(PaymentException.class)
    public ResponseEntity<?> handlePaymentException(PaymentException e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(NotificationException.class)
    public ResponseEntity<?> handleNotificationException(NotificationException e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
        String errorMessage = e.getBindingResult().getFieldErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(", "));

        return ResponseEntity.badRequest().body(errorMessage);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<?> handleMissingServletRequestParameterException(MissingServletRequestParameterException e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }

}
