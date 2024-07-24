package com.prateek.web.springrestdemo.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.prateek.web.springrestdemo.domain.entities.Beer;
import com.prateek.web.springrestdemo.domain.entities.BeerOrder;
import com.prateek.web.springrestdemo.domain.entities.Customer;

import jakarta.transaction.Transactional;

@SpringBootTest
public class BeerOrderRepositoryTest {
    @Autowired
    BeerOrderRepository beerOrderRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    BeerRepository beerRepository;

    Customer testCustomer;
    Beer testBeer;

    @BeforeEach
    void setUp() {
        this.testCustomer = customerRepository.findAll().get(0);
        this.testBeer = beerRepository.findAll().get(0);
    }

    @Transactional
    @Test
    void testBeerOrders() {
        BeerOrder beerOrder = BeerOrder.builder()
                .customerRef("Test order")
                .customer(testCustomer)
                .build();

        /**
         * - we need to add @Transactional otherwise we will get
         * LazyInitializationException
         * - The most common scenario for LazyInitializationException is when we try to
         * access a lazily initialized collection or proxy object after the Hibernate
         * session in this case it is Customer.getBeerOrders()
         * that fetched the parent entity has already been closed.
         * - Always try to persist an entity object before making any updates to its
         * properties
         * - saveAndFlush immideately persists data and shows bidirectional relation
         * - nested customer will have beer orders after saveAndFlush but not with just
         * save
         */
        // BeerOrder savedBeerOrder = beerOrderRepository.saveAndFlush(beerOrder);

        // save and flush has performance overhead so we use custom association logic
        // in BeerOrder and Customer to populate the relationships
        BeerOrder savedBeerOrder = beerOrderRepository.save(beerOrder);
        assertEquals(savedBeerOrder.getCustomer().getBeerOrders().size(), 1);
        savedBeerOrder.getCustomer().getBeerOrders().stream().findFirst()
                .ifPresent(found -> {
                    assertEquals(found.getCustomerRef(), "Test order");
                });

    }
}
