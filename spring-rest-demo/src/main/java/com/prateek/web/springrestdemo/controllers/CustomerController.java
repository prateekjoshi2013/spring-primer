package com.prateek.web.springrestdemo.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import com.prateek.web.springrestdemo.domain.exceptions.NoCustomerException;
import com.prateek.web.springrestdemo.model.CustomerDTO;
import com.prateek.web.springrestdemo.services.CustomerService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequiredArgsConstructor
public class CustomerController {
    private static final String CUSTOMER_ID = "/{customerId}";
    public static final String API_V1_CUSTOMER = "/api/v1/customer";
    public static final String API_V1_CUSTOMER_PATH_ID = API_V1_CUSTOMER + CUSTOMER_ID;
    private final CustomerService customerServiceImpl;

    @GetMapping(API_V1_CUSTOMER)
    List<CustomerDTO> listCustomers() {
        return this.customerServiceImpl.listCustomers();
    }

    @GetMapping(API_V1_CUSTOMER_PATH_ID)
    public CustomerDTO getCustomerById(@PathVariable("customerId") UUID customerId) {
        return this.customerServiceImpl.getCustomerById(customerId).get();
    }

    @PostMapping(API_V1_CUSTOMER)
    public ResponseEntity<CustomerDTO> postCustomer(@RequestBody CustomerDTO customer) {
        CustomerDTO savedCustomer = this.customerServiceImpl.saveCustomer(customer);
        HttpHeaders headers = new HttpHeaders();
        headers.set("location", API_V1_CUSTOMER + savedCustomer.getId());
        return new ResponseEntity<CustomerDTO>(savedCustomer, headers, HttpStatus.CREATED);
    }

    @PutMapping(API_V1_CUSTOMER_PATH_ID)
    public ResponseEntity<CustomerDTO> putMethodName(@PathVariable UUID customerId, @RequestBody CustomerDTO customer) {
        CustomerDTO updatedCustomer = this.customerServiceImpl.updateCustomerById(customerId, customer).get();
        HttpHeaders headers = new HttpHeaders();
        headers.set("location", API_V1_CUSTOMER + updatedCustomer.getId());
        return new ResponseEntity<CustomerDTO>(updatedCustomer, headers, HttpStatus.ACCEPTED);
    }

    @DeleteMapping(API_V1_CUSTOMER_PATH_ID)
    public ResponseEntity<Object> deleteCustomerById(@PathVariable UUID customerId) {
        this.customerServiceImpl.deleteCustomerById(customerId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ExceptionHandler(NoCustomerException.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex, WebRequest request) {
        ResponseEntity<ErrorResponse> responseEntity = new ResponseEntity<>(
                new ErrorResponse(
                        ex.getMessage(),
                        request.getDescription(false)),
                HttpStatus.NOT_FOUND);
        return responseEntity;
    }

}