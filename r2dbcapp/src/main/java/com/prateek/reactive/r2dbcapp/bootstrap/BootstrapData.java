package com.prateek.reactive.r2dbcapp.bootstrap;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.prateek.reactive.r2dbcapp.domain.Beer;
import com.prateek.reactive.r2dbcapp.repositories.BeerRepository;

@Component
public class BootstrapData implements CommandLineRunner {

    private List<Beer> beers = Arrays.asList(
            Beer.builder()
                    .beerName("indian pale ale")
                    .price(BigDecimal.valueOf(12.023))
                    .beerStyle("PALE_ALE")
                    .quantityOnHand(12)
                    .upc("upc")
                    .build(),
            Beer.builder()
                    .beerName("Fosters")
                    .price(BigDecimal.valueOf(10.023))
                    .beerStyle("PILSNER")
                    .quantityOnHand(10)
                    .upc("upc")
                    .build(),
            Beer.builder()
                    .beerName("Kingfisher")
                    .price(BigDecimal.valueOf(10.023))
                    .beerStyle("STOUT")
                    .quantityOnHand(10)
                    .upc("upc")
                    .build());

    @Autowired
    BeerRepository beerRepository;

    @Override
    public void run(String... args) throws Exception {
        beerRepository.count().filter(cnt -> cnt == 0).subscribe(cnt -> {
            beerRepository.saveAll(beers).subscribe(savedBeer -> System.out.println(savedBeer));
        });
        beerRepository.count().subscribe((cnt) -> {
            System.out.println("total number of items: " + cnt);
        });
    }

}
