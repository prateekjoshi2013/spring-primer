package com.prateek.web.springrestdemo.bootstrap;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.prateek.web.springrestdemo.repositories.BeerRepository;
import com.prateek.web.springrestdemo.repositories.CustomerRepository;

import lombok.SneakyThrows;

@DataJpaTest
public class BootstrapDataTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private BeerRepository beerRepository;

    private BootstrapData bootstrapData;

    @BeforeEach
    void setUp() {
        bootstrapData = new BootstrapData(beerRepository, customerRepository);
    }

    @SneakyThrows
    @Test
    void testRun() {
        bootstrapData.run();
        assertEquals(beerRepository.count(), 3l);
        assertEquals(customerRepository.count(), 3l);
    }
}
