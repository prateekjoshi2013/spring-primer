package com.prateek.web.springrestdemo.domain.exceptions;

public class NoCustomerException extends RuntimeException {
    public NoCustomerException(String message) {
        super(message);
    }
}
