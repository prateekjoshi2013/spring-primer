package com.prateek.reactive.mongo.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;

import com.prateek.reactive.mongo.config.AbstractTestContainer;
import com.prateek.reactive.mongo.domain.BeerDTO;
import com.prateek.reactive.mongo.mappers.BeerMapper;
import com.prateek.reactive.mongo.model.Beer;
import com.prateek.reactive.mongo.repositories.BeerRepository;

import lombok.SneakyThrows;
import reactor.core.publisher.Mono;

// Test do not support @Transactional so we need to run the tests in a particular order since it shares the context
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BeerServiceImplTest extends AbstractTestContainer {

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

    public BeerDTO getTestBeer() {
        return BeerDTO.builder()
                .beerName("My Bear")
                .beerStyle("PALE_ALE")
                .price(BigDecimal.TEN)
                .quantityOnHand(10)
                .upc("upc")
                .build();
    }

    @Order(1)
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

    @Order(2)
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

    @Order(3)
    @Test
    void testListBeers() {
        AtomicBoolean finishedFetching = new AtomicBoolean(false);
        beerService.listBeers().collectList().subscribe(beers -> {
            System.out.println(beers);
            assertThat(beers.size()).isEqualTo(2);
            finishedFetching.set(true);

        });
        await().untilTrue(finishedFetching); // if we dont awit here mongo connection is closed before
    }

    @Order(4)
    @Test
    void testUpdateBeer() {
        AtomicBoolean finishedUpdating = new AtomicBoolean(false);
        BeerDTO beerDto = getTestBeer();
        String beerName = "Updated BeerName";
        beerDto.setBeerName(beerName);
        beerService.updateBeer(savedBeerId, beerDto).subscribe(updatedBeer -> {
            System.out.println(updatedBeer);
            assertThat(updatedBeer.getId()).isEqualTo(savedBeerId);
            assertThat(updatedBeer.getBeerName()).isEqualTo(beerName);
            finishedUpdating.set(true);

        });
        await().untilTrue(finishedUpdating); // if we dont awit here mongo connection is closed before
    }

    @Order(5)
    @Test
    void testDeleteBeer() {
        AtomicBoolean finishedDeleting = new AtomicBoolean(false);
        beerService.deleteBeer(savedBeerId).doOnSuccess(unused -> {
            finishedDeleting.set(true);
        }).subscribe();
        await().untilTrue(finishedDeleting); // if we dont await here mongo connection is closed before
    }

}
