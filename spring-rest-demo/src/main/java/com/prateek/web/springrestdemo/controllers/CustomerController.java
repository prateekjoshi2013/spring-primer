package com.prateek.web.springrestdemo.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prateek.web.springrestdemo.model.Customer;
import com.prateek.web.springrestdemo.services.CustomerService;

import lombok.AllArgsConstructor;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RequestMapping("/api/v1/customer")
@RestController
@AllArgsConstructor
public class CustomerController {
    private CustomerService customerServiceImpl;

    @GetMapping("")
    List<Customer> listCustomers() {
        return this.customerServiceImpl.listCustomers();
    }

    @GetMapping("{customerId}")
    public Customer getCustomerById(@PathVariable("customerId") UUID customerId) {
        return this.customerServiceImpl.getCustomerById(customerId);
    }

    @PostMapping()
    public ResponseEntity<Customer> postCustomer(@RequestBody Customer customer) {
        Customer savedCustomer = this.customerServiceImpl.saveCustomer(customer);
        HttpHeaders headers = new HttpHeaders();
        headers.set("location", "/api/v1/customer" + savedCustomer.getId());
        return new ResponseEntity<Customer>(savedCustomer, headers, HttpStatus.CREATED);
    }

    @PutMapping("/{customerId}")
    public ResponseEntity<Customer> putMethodName(@PathVariable UUID customerId, @RequestBody Customer customer) {
        Customer updatedCustomer = this.customerServiceImpl.updateCustomerById(customerId, customer);
        HttpHeaders headers = new HttpHeaders();
        headers.set("location", "/api/v1/customer" + updatedCustomer.getId());
        return new ResponseEntity<Customer>(updatedCustomer, headers, HttpStatus.ACCEPTED);
    }
}