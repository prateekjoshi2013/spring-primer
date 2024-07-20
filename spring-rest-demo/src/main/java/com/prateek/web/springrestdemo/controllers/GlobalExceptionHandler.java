package com.prateek.web.springrestdemo.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.prateek.web.springrestdemo.domain.exceptions.NoBeerFoundException;

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
}
