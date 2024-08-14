package com.prateek.webclient.client;

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
        beerClientImpl.listBeer().subscribe(response -> {
            System.out.println(response);
        });

        Thread.sleep(1000l);
    }
}
