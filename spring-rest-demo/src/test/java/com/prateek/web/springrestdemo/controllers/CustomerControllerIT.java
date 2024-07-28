package com.prateek.web.springrestdemo.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.EnabledIf;

import com.prateek.web.springrestdemo.domain.entities.Customer;
import com.prateek.web.springrestdemo.domain.exceptions.NoCustomerException;
import com.prateek.web.springrestdemo.mappers.CustomerMapper;
import com.prateek.web.springrestdemo.model.CustomerDTO;
import com.prateek.web.springrestdemo.repositories.CustomerRepository;

import jakarta.transaction.Transactional;

@EnabledIf(value = "#{{'default','localmysql'}.contains(environment.getActiveProfiles()[0])}", loadContext = true)
@SpringBootTest
public class CustomerControllerIT {
    @Autowired
    CustomerController customerController;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    CustomerMapper customerMapperImpl;

    @Test
    void testFindById() {
        Customer customer = customerRepository.findAll().get(0);
        CustomerDTO found = customerController.getCustomerById(customer.getId());
        assertEquals(customer.getId(), found.getId());

    }

    @Test
    void testFindByIdNotFound() {
        assertThrows(NoCustomerException.class, () -> {
            customerController.getCustomerById(UUID.randomUUID());
        });
    }

    @Test
    void testListCustomers() {
        assertEquals(3l, customerController.listCustomers().size());
    }

    @Transactional
    @Rollback
    @Test
    void testUpdateNonExistingCustomer() {
        UUID randomUUID = UUID.randomUUID();
        Customer customer = customerRepository.findAll().get(0);
        CustomerDTO customerDto = customerMapperImpl.customerToCustomerDto(customer);
        customerDto.setId(null);
        customerDto.setVersion(null);
        customerDto.setCustomerName("updated name");
        Exception exception = assertThrows(NoCustomerException.class, () -> {
            customerController.putMethodName(randomUUID, customerDto);
        });
        assertEquals(exception.getMessage(), "Customer with id: " + randomUUID + " not found");
    }

    @Transactional
    @Rollback
    @Test
    void testUpdateExistingCustomer() {
        Customer customer = customerRepository.findAll().get(0);
        CustomerDTO customerDto = customerMapperImpl.customerToCustomerDto(customer);
        customerDto.setId(null);
        customerDto.setVersion(null);
        customerDto.setCustomerName("updated name");
        customerController.putMethodName(customer.getId(), customerDto);
        customerRepository.findById(customer.getId())
                .ifPresent(updatedCustomer -> assertEquals(updatedCustomer.getCustomerName(), "updated name"));
        ;
    }

    @Rollback
    @Transactional
    @Test
    void testSaveCustomer() {
        ResponseEntity<CustomerDTO> responseEntity = customerController
                .postCustomer(CustomerDTO.builder().customerName("My customer").build());
        assertEquals("My customer", responseEntity.getBody().getCustomerName());
    }

    @Rollback
    @Transactional
    @Test
    void testDeleteExistingCustomer() {
        Customer customer = customerRepository.findAll().get(0);
        customerController.deleteCustomerById(customer.getId());
    }

    @Rollback
    @Transactional
    @Test
    void testDeleteNonExistingCustomer() {
        UUID uuid = UUID.randomUUID();
        assertThrows(NoCustomerException.class, () -> {
            customerController.deleteCustomerById(uuid);
        });
    }

}
