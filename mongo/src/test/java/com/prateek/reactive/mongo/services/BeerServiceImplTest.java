package com.prateek.reactive.mongo.services;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.prateek.reactive.mongo.domain.BeerDTO;
import com.prateek.reactive.mongo.mappers.BeerMapper;
import com.prateek.reactive.mongo.model.Beer;
import com.prateek.reactive.mongo.repositories.BeerRepository;

import lombok.SneakyThrows;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.mockito.Answers.valueOf;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BeerServiceImplTest {

    @Autowired
    BeerRepository beerRepository;

    @Autowired
    BeerMapper beerMapper;

    @Autowired
    BeerService beerService;

    private String savedBeerId;

    @BeforeAll
    void setUp() {
        Beer testBeer = beerMapper.beerDTOtoBeer(getTestBeer());
        AtomicBoolean finishedSaving = new AtomicBoolean(false);
        beerRepository.save(testBeer).subscribe(beer -> {
            System.out.println(beer);
            assertThat(beer.getId()).isNotEmpty();
            savedBeerId = beer.getId();
            finishedSaving.set(true);
        });
        await().untilTrue(finishedSaving);
    }

    @AfterAll
    void cleanUp() {
        beerRepository.deleteAll().block();
    }

    public BeerDTO getTestBeer() {
        return BeerDTO.builder()
                .beerName("My Bear")
                .beerStyle("PALE_ALE")
                .price(BigDecimal.TEN)
                .quantityOnHand(10)
                .upc("upc")
                .build();
    }

    @Test
    @SneakyThrows
    void testSaveBeer() {
        BeerDTO testBeer = getTestBeer();
        AtomicBoolean finishedSaving = new AtomicBoolean(false);
        beerService.saveBeer(testBeer).subscribe(beer -> {
            System.out.println(beer);
            assertThat(beer.getId()).isNotEmpty();
            finishedSaving.set(true);

        });
        await().untilTrue(finishedSaving); // if we dont awit here mongo connection is closed before
    }

    @Test
    void testFindById() {
        AtomicBoolean finishedSaving = new AtomicBoolean(false);
        beerService.findById(savedBeerId).subscribe(beer -> {
            System.out.println(beer);
            assertThat(beer.getId()).isEqualTo(savedBeerId);
            finishedSaving.set(true);

        });
        await().untilTrue(finishedSaving); // if we dont awit here mongo connection is closed before
    }
}
