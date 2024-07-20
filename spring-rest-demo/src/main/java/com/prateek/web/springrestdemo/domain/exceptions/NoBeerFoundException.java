package com.prateek.web.springrestdemo.domain.exceptions;

public class NoBeerFoundException extends RuntimeException {
    public NoBeerFoundException(String beerId) {
        super("Beer with id: " + beerId + " not found");
    }
}
