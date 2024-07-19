package com.prateek.web.springrestdemo.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.prateek.web.springrestdemo.model.Customer;

@SpringBootTest
public class CustomerControllerTest {
    @Autowired
    private CustomerController customerController;

    @Test
    void testGetCustomerById() {
        UUID customerId = this.customerController.listCustomers().get(0).getId();
        Customer customer = this.customerController.getCustomerById(customerId);
        assertEquals(customerId, customer.getId());
    }

    @Test
    void testListCustomers() {
        int size = this.customerController.listCustomers().size();
        assertEquals(size, 3);
    }
}
