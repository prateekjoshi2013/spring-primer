package com.prateek.web.springrestdemo.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Not a valid id")
public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
