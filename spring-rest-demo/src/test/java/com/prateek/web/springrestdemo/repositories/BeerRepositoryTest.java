package com.prateek.web.springrestdemo.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.prateek.web.springrestdemo.domain.entities.Beer;
import com.prateek.web.springrestdemo.model.BeerStyle;

import jakarta.validation.ConstraintViolationException;

@DataJpaTest
public class BeerRepositoryTest {
    @Autowired
    BeerRepository beerRepository;

    @Test
    void testSaveBeer() {
        Beer beer = Beer.builder()
                .beerName("My Beer")
                .beerStyle(BeerStyle.ALE)
                .price(BigDecimal.valueOf(1.12))
                .upc("upc")
                .build();
        Beer savedBeer = beerRepository.save(beer);
        // the flush operation happens asynchronously and actually saves the entity to
        // db and that is when the validation happens
        // if we do not add explicitly flush here the test finishes before the actual
        // flush operation happens
        beerRepository.flush();
        assertThat(savedBeer).isNotNull();
        assertThat(savedBeer.getId()).isNotNull();
    }

    @Test
    void testSaveBeerConstraintViolation() {
        ConstraintViolationException ex = assertThrows(ConstraintViolationException.class, () -> {
            Beer savedBeer = beerRepository.save(Beer.builder().beerName("My Beer").build());
            // the flush operation happens asynchronously and actually saves the entity to
            // db and that is when the validation happens
            // if we do not add explicitly flush here the test finishes before the actual
            // flush operation happens
            beerRepository.flush();
        });
        assertThat(ex).isNotNull();
    }
}
