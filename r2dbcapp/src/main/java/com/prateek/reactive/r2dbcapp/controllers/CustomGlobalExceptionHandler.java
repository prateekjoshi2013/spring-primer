package com.prateek.reactive.r2dbcapp.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;

import com.prateek.reactive.r2dbcapp.model.CustomResponseExceptionDTO;

@ControllerAdvice
public class CustomGlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<CustomResponseExceptionDTO> responseStatusExceptionHandler(
            ResponseStatusException exception, ServerWebExchange exchange) {
        return new ResponseEntity<>(
                CustomResponseExceptionDTO
                        .builder()
                        .status(HttpStatus.NOT_FOUND.name())
                        .path(exchange.getRequest().getPath().toString())
                        .error(exception.getReason())
                        .build(),
                HttpStatus.NOT_FOUND);
    }

}
