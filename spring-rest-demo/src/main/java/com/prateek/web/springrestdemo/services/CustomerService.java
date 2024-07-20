package com.prateek.web.springrestdemo.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.prateek.web.springrestdemo.model.CustomerDTO;

public interface CustomerService {

    Optional<CustomerDTO> getCustomerById(UUID customerId);

    List<CustomerDTO> listCustomers();

    CustomerDTO saveCustomer(CustomerDTO customer);

    Optional<CustomerDTO> updateCustomerById(UUID customerId, CustomerDTO customer);

    void deleteCustomerById(UUID customerId);
}
