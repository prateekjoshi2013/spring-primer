package com.prateek.web.springrestdemo.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.prateek.web.springrestdemo.model.Customer;

public interface CustomerService {

    Optional<Customer> getCustomerById(UUID customerId);

    List<Customer> listCustomers();

    Customer saveCustomer(Customer customer);

    Optional<Customer> updateCustomerById(UUID customerId, Customer customer);

    void deleteCustomerById(UUID customerId);
}
