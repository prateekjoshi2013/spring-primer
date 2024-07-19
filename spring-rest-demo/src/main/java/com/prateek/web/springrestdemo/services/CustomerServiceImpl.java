package com.prateek.web.springrestdemo.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.prateek.web.springrestdemo.model.Customer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {
    private Map<UUID, Customer> customerMap = Stream.of(
            Customer.builder()
                    .version(1)
                    .id(UUID.randomUUID())
                    .customerName("John")
                    .createdDate(LocalDateTime.now())
                    .lastModifiedDate(LocalDateTime.now())
                    .build(),
            Customer.builder()
                    .version(2)
                    .id(UUID.randomUUID())
                    .customerName("Jane")
                    .createdDate(LocalDateTime.now())
                    .lastModifiedDate(LocalDateTime.now())
                    .build(),
            Customer.builder()
                    .version(3)
                    .id(UUID.randomUUID())
                    .customerName("Dane")
                    .createdDate(LocalDateTime.now())
                    .lastModifiedDate(LocalDateTime.now())
                    .build())
            .collect(Collectors.toMap(customer -> customer.getId(), customer -> customer));

    @Override
    public Customer getCustomerById(UUID customerId) {
        Customer customer = this.customerMap.get(customerId);
        log.info("Sending customer {}:{}", customerId, customer);
        return customer;
    }

    @Override
    public List<Customer> listCustomers() {
        log.info("Sending list of customers");
        return this.customerMap.values().stream().collect(Collectors.toList());
    }
}
