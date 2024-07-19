package com.prateek.web.springrestdemo.services;

import java.util.List;
import java.util.UUID;

import com.prateek.web.springrestdemo.model.Customer;

public interface CustomerService {

    Customer getCustomerById(UUID customerId);

    List<Customer> listCustomers();
}