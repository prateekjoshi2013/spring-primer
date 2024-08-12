package com.prateek.reactive.mongo.bootstrap;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.prateek.reactive.mongo.model.Beer;
import com.prateek.reactive.mongo.repositories.BeerRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BootstrapData implements CommandLineRunner {
    private final BeerRepository beerRepository;

    @Override
    public void run(String... args) throws Exception {
        beerRepository.deleteAll().doOnSuccess(success -> {
            loadData();
        }).subscribe();
    }

    public void loadData() {
        List<Beer> beers = Arrays.asList(
                Beer.builder()
                        .beerName("Beer 1")
                        .beerStyle("PALE_ALE")
                        .price(BigDecimal.valueOf(10l))
                        .quantityOnHand(10)
                        .upc("upc")
                        .build(),
                Beer.builder()
                        .beerName("Beer 2")
                        .beerStyle("ALE")
                        .price(BigDecimal.valueOf(10l))
                        .quantityOnHand(10)
                        .upc("upc")
                        .build(),
                Beer.builder()
                        .beerName("Beer 3")
                        .beerStyle("LAGER")
                        .price(BigDecimal.valueOf(10l))
                        .quantityOnHand(10)
                        .upc("upc")
                        .build()

        );
        beerRepository.saveAll(beers).collectList().block();
    }

}
