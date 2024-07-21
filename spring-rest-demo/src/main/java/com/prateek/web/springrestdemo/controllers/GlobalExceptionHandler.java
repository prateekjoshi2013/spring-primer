package com.prateek.web.springrestdemo.controllers;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.prateek.web.springrestdemo.domain.exceptions.NoBeerFoundException;

import jakarta.validation.ConstraintViolation;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoBeerFoundException.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex, WebRequest request) {
        ResponseEntity<ErrorResponse> responseEntity = new ResponseEntity<>(
                new ErrorResponse(
                        ex.getMessage(),
                        request.getDescription(false)),
                HttpStatus.NOT_FOUND);
        return responseEntity;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleContraintValidationErrors(MethodArgumentNotValidException ex) {
        List<Map<String, String>> collect = ex.getFieldErrors().stream()
                .map(fieldError -> {
                    Map<String, String> errorMap = new HashMap<>();
                    errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
                    return errorMap;
                }).collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(collect);
    }

}
