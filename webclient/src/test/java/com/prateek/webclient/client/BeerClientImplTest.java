package com.prateek.webclient.client;

import static org.awaitility.Awaitility.await;

import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.SneakyThrows;

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
}
