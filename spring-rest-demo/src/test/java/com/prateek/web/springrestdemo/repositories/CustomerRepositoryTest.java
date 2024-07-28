package com.prateek.web.springrestdemo.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.EnabledIf;

import com.prateek.web.springrestdemo.domain.entities.Customer;

@EnabledIf(value = "#{{'default'}.contains(environment.getActiveProfiles()[0])}", loadContext = false)
@DataJpaTest
public class CustomerRepositoryTest {

    @Autowired
    CustomerRepository customerRepository;

    @Test
    void testCustomerSave() {
        Customer savedCustomer = customerRepository
                .save(Customer.builder()
                        .customerName("John Doe")
                        .build());
        assertThat(savedCustomer).isNotNull();
        assertThat(savedCustomer.getId()).isNotNull();

    }
}
