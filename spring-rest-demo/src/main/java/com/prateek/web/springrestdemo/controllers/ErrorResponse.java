package com.prateek.web.springrestdemo.controllers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

// for jackson the ErrorResponse needs getters and Setters
@AllArgsConstructor
@Getter
@Setter
public class ErrorResponse {
    private String message;
    private String details;
}