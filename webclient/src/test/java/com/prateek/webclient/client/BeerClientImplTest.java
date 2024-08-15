package com.prateek.webclient.client;

import java.math.BigDecimal;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.prateek.webclient.model.BeerDTO;

import lombok.SneakyThrows;
import reactor.test.StepVerifier;

@SpringBootTest
public class BeerClientImplTest {

    @Autowired
    BeerClient beerClientImpl;

    @Test
    @SneakyThrows
    void testListBeer() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        beerClientImpl.listBeer().subscribe(response -> {
            System.out.println(response);
            atomicBoolean.set(true);
        });
        await().untilTrue(atomicBoolean);
    }

    @Test
    @SneakyThrows
    void testListBeerMap() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        beerClientImpl.listBeerMap().subscribe(response -> {
            System.out.println(response);
            atomicBoolean.set(true);
        });
        await().untilTrue(atomicBoolean);

    }

    @Test
    void testListBeerJsonNode() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        beerClientImpl.listBeerJsonNode().subscribe(response -> {
            System.out.println(response);
            atomicBoolean.set(true);
        });
        await().untilTrue(atomicBoolean);
    }

    @Test
    void testListBeerDto() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        beerClientImpl.listBeerDto().subscribe(response -> {
            System.out.println(response);
            atomicBoolean.set(true);
        });
        await().untilTrue(atomicBoolean);
    }

    @Test
    void testGetByBeerIdByContent() {

        List<BeerDTO> collectedItems = new ArrayList<>();
        StepVerifier.create(
                beerClientImpl.listBeerDto()
                        .flatMap(beer -> beerClientImpl.getByBeerId(beer.getId())))
                .recordWith(() -> collectedItems) // Collect items
                .thenConsumeWhile(item -> true) // Continue until all items are consumed
                .verifyComplete();
        System.out.println(collectedItems);
        assertTrue(
                collectedItems.stream().filter(beer -> beer.getBeerStyle().equals("PALE_ALE")).findAny().isPresent());
    }

    // @Test
    // void testGetByBeerIdCount() {

    //     StepVerifier.create(
    //             beerClientImpl.listBeerDto()
    //                     .flatMap(beer -> beerClientImpl.getByBeerId(beer.getId())))
    //             .expectNextCount(3) // Expect exactly three elements to be emitted
    //             .verifyComplete();
    // }

    @Test
    void testGetByBeerStyle() {
        List<BeerDTO> collectedItems = new ArrayList<>();
        StepVerifier.create(beerClientImpl.getBeersByStyle("PALE_ALE"))
                .recordWith(() -> collectedItems) // Collect items
                .thenConsumeWhile(item -> true) // Continue until all items are consumed
                .verifyComplete();
        System.out.println(collectedItems);
        assertTrue(collectedItems.size() > 1);
    }

    @Test
    void testCreateBeer() {
        List<BeerDTO> collectedItems = new ArrayList<>();
        StepVerifier.create(beerClientImpl.createBeer(getBeerDTO()))
                .recordWith(() -> collectedItems) // Collect items
                .thenConsumeWhile(item -> true) // Continue until all items are consumed
                .verifyComplete();
        System.out.println(collectedItems);
        assertTrue(collectedItems.size() == 1);
    }

    BeerDTO getBeerDTO() {
        return BeerDTO.builder()
                .beerName("My Crafted Beer")
                .beerStyle("ALE")
                .price(BigDecimal.valueOf(10l))
                .quantityOnHand(23)
                .upc("upc").build();
    }


}
