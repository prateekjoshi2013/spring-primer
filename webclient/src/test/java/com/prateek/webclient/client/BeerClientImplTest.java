package com.prateek.webclient.client;

import java.math.BigDecimal;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
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
                collectedItems.size() > 0);
    }

    // @Test
    // void testGetByBeerIdCount() {

    // StepVerifier.create(
    // beerClientImpl.listBeerDto()
    // .flatMap(beer -> beerClientImpl.getByBeerId(beer.getId())))
    // .expectNextCount(3) // Expect exactly three elements to be emitted
    // .verifyComplete();
    // }

    @Test
    void testGetByBeerStyle() {
        List<BeerDTO> collectedItems = new ArrayList<>();
        StepVerifier.create(beerClientImpl.getBeersByStyle("ALE"))
                .recordWith(() -> collectedItems) // Collect items
                .thenConsumeWhile(item -> true) // Continue until all items are consumed
                .verifyComplete();
        System.out.println(collectedItems);
        assertTrue(collectedItems.size() > 0);
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

    @Test
    void testUpdateBeerById() {
        BeerDTO beerDTO = getBeerDTO();
        ;
        List<BeerDTO> collectedItems = new ArrayList<>();
        StepVerifier.create(
                beerClientImpl.listBeerDto().last()
                        .flatMap(beer -> {
                            beerDTO.setBeerName(beer.getBeerName() + "Updated");
                            return beerClientImpl.updateBeer(beer.getId(), beerDTO);
                        }))
                .recordWith(() -> collectedItems) // Collect items
                .thenConsumeWhile(item -> true) // Continue until all items are consumed
                .verifyComplete();
        System.out.println(collectedItems);
        assertTrue(
                collectedItems.stream().filter(beer -> beer.getBeerName().contains("Updated")).findAny().isPresent());
    }

    @Test
    void testDeleteBeerById() {
        AtomicBoolean finishedDeleting = new AtomicBoolean(false);
        beerClientImpl.listBeerDto().next()
                .flatMap(beer -> {
                    return beerClientImpl.deleteBeer(beer.getId());
                })
                .doOnSuccess(success -> {
                    assertNull(success);
                    finishedDeleting.set(true);
                })
                .subscribe();
        await().untilTrue(finishedDeleting);
    }

    BeerDTO getBeerDTO() {
        return BeerDTO.builder()
                .beerName("My Crafted Beer")
                .beerStyle("PALE_ALE")
                .price(BigDecimal.valueOf(10l))
                .quantityOnHand(23)
                .upc("upc").build();
    }

}
