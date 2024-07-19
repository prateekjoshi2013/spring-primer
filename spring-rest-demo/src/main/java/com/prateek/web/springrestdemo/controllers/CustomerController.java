package com.prateek.web.springrestdemo.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prateek.web.springrestdemo.model.Customer;
import com.prateek.web.springrestdemo.services.CustomerService;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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

}