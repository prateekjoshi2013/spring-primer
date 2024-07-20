package com.prateek.web.springrestdemo.domain.exceptions;

public class NoBeerFoundException extends RuntimeException {
    public NoBeerFoundException(String message) {
        super(message);
    }
}
