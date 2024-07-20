package com.prateek.web.springrestdemo.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.prateek.web.springrestdemo.domain.exceptions.NoCustomerException;
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
                                        .customerName("Kane")
                                        .createdDate(LocalDateTime.now())
                                        .lastModifiedDate(LocalDateTime.now())
                                        .build())
                        .collect(Collectors.toMap(customer -> customer.getId(), customer -> customer));

        @Override
        public Optional<Customer> getCustomerById(UUID customerId) {
                return Optional.ofNullable(this.customerMap.get(customerId))
                                .map(customer -> {
                                        log.info("Sending customer {}:{}", customerId, customer);
                                        return Optional.of(customer);

                                }).orElseThrow(() -> new NoCustomerException(customerId.toString()));
        }

        @Override
        public List<Customer> listCustomers() {
                log.info("Sending list of customers");
                return this.customerMap.values().stream().collect(Collectors.toList());
        }

        @Override
        public Customer saveCustomer(Customer customer) {
                Customer savedCustomer = Customer
                                .builder()
                                .version(customer.getVersion())
                                .id(UUID.randomUUID())
                                .customerName(customer.getCustomerName())
                                .createdDate(LocalDateTime.now())
                                .lastModifiedDate(LocalDateTime.now())
                                .build();
                this.customerMap.put(savedCustomer.getId(), savedCustomer);
                log.info("saved customer {}", customer);
                return savedCustomer;
        }

        @Override
        public Optional<Customer> updateCustomerById(UUID customerId, Customer customer) {
                return Optional.ofNullable(this.customerMap.get(customerId))
                                .map(oldCustomer -> {
                                        oldCustomer.setCustomerName(customer.getCustomerName());
                                        oldCustomer.setLastModifiedDate(LocalDateTime.now());
                                        log.info("updated customer: {}", oldCustomer);
                                        return Optional.of(oldCustomer);
                                }).orElseThrow(() -> new NoCustomerException(customerId.toString()));
        }

        @Override
        public void deleteCustomerById(UUID customerId) {
                Optional.ofNullable(this.customerMap.get(customerId))
                                .ifPresent(customer -> {
                                        this.customerMap.remove(customerId);
                                        log.info("deleting customer with id :{}", customerId);
                                });
        }

}
