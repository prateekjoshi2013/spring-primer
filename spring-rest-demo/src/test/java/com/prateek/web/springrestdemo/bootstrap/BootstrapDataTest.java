package com.prateek.web.springrestdemo.bootstrap;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.EnabledIf;

import com.prateek.web.springrestdemo.mappers.BeerCsvMapper;
import com.prateek.web.springrestdemo.mappers.BeerCsvMapperImpl;
import com.prateek.web.springrestdemo.repositories.BeerRepository;
import com.prateek.web.springrestdemo.repositories.CustomerRepository;
import com.prateek.web.springrestdemo.services.BeerCsvService;
import com.prateek.web.springrestdemo.services.BeerCsvServiceImpl;

import lombok.SneakyThrows;
@EnabledIf(value = "#{{'default'}.contains(environment.getActiveProfiles()[0])}", loadContext = false)
@DataJpaTest
@Import({ BeerCsvServiceImpl.class, BeerCsvMapperImpl.class })
public class BootstrapDataTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private BeerRepository beerRepository;

    @Autowired
    private BeerCsvService beerCsvServiceImpl;

    @Autowired
    private BeerCsvMapper beerCsvMapperImpl;

    private BootstrapData bootstrapData;

    @BeforeEach
    void setUp() {
        bootstrapData = new BootstrapData(beerRepository, customerRepository, beerCsvServiceImpl, beerCsvMapperImpl);
    }

    @SneakyThrows
    @Test
    void testRun() {
        bootstrapData.run();
        assertEquals(beerRepository.count(), 2410l);
        assertEquals(customerRepository.count(), 3l);
    }
}
