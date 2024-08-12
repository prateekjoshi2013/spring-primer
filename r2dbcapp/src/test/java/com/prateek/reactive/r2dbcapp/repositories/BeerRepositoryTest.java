package com.prateek.reactive.r2dbcapp.repositories;

import java.math.BigDecimal;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import com.prateek.reactive.r2dbcapp.config.DatabaseConfig;
import com.prateek.reactive.r2dbcapp.domain.Beer;

@DataR2dbcTest
@Import(DatabaseConfig.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BeerRepositoryTest {

    @Autowired
    BeerRepository beerRepository;

    @Test
    void testSaveBeer() {
        beerRepository
                .save(getTestBeer())
                .subscribe(
                        savedBeer -> System.out.println(savedBeer));
    }

    Beer getTestBeer() {
        return Beer.builder()
                .beerName("Kingfisher")
                .beerStyle("lager")
                .price(BigDecimal.valueOf(10.5f))
                .quantityOnHand(12)
                .upc("upc")
                .build();
    }

    @AfterAll
    void teardown() {
        System.out.println("Running after all tests");
        // Cleanup logic
        beerRepository
                .deleteAll().block();
    }
}
