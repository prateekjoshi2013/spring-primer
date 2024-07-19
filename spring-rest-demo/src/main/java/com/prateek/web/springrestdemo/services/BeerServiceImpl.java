package com.prateek.web.springrestdemo.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.prateek.web.springrestdemo.model.Beer;
import com.prateek.web.springrestdemo.model.BeerStyle;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BeerServiceImpl implements BeerService {
    private Map<UUID, Beer> beerMap = Stream.of(
            Beer.builder()
                    .id(UUID.randomUUID())
                    .beerName("indian pale ale")
                    .price(BigDecimal.valueOf(12.023))
                    .beerStyle(BeerStyle.PALE_ALE)
                    .quantityOnHand(12)
                    .upc("upc")
                    .version(1)
                    .createdDate(LocalDateTime.now())
                    .updateDate(LocalDateTime.now())
                    .build(),
            Beer.builder()
                    .id(UUID.randomUUID())
                    .beerName("Fosters")
                    .price(BigDecimal.valueOf(10.023))
                    .beerStyle(BeerStyle.PILSNER)
                    .quantityOnHand(10)
                    .upc("upc")
                    .version(2)
                    .createdDate(LocalDateTime.now())
                    .updateDate(LocalDateTime.now())
                    .build(),
            Beer.builder()
                    .id(UUID.randomUUID())
                    .beerName("Kingfisher")
                    .price(BigDecimal.valueOf(10.023))
                    .beerStyle(BeerStyle.STOUT)
                    .quantityOnHand(10)
                    .upc("upc")
                    .version(2)
                    .createdDate(LocalDateTime.now())
                    .updateDate(LocalDateTime.now())
                    .build())
            .collect(Collectors.toMap(beer -> beer.getId(), beer -> beer));

    public List<Beer> listBeers() {
        log.info("sending the list of beers");
        return this.beerMap.values().stream().toList();
    }

    @Override
    public Beer getBeerById(UUID id) {
        Beer beer = this.getBeerById(id);
        log.info("sending the beer object with id {}: {}", id, beer);
        return beer;
    }

}
