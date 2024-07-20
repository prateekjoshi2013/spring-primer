package com.prateek.web.springrestdemo.domain.exceptions;

public class NoCustomerException extends RuntimeException {
    public NoCustomerException(String id) {
        super("Customer with id: " + id + " not found");
    }
}
