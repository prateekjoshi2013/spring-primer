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
                                        .version(2)
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
                Beer beer = this.beerMap.get(id);
                log.info("sending the beer object with id {}: {}", id, beer);
                return beer;
        }

        @Override
        public Beer saveNewBeer(Beer beer) {
                Beer savedBeer = Beer.builder()
                                .id(UUID.randomUUID())
                                .beerName(beer.getBeerName())
                                .price(beer.getPrice())
                                .beerStyle(beer.getBeerStyle())
                                .quantityOnHand(beer.getQuantityOnHand())
                                .upc(beer.getUpc())
                                .version(beer.getVersion())
                                .createdDate(LocalDateTime.now())
                                .updateDate(LocalDateTime.now())
                                .build();

                this.beerMap.put(savedBeer.getId(), savedBeer);
                return savedBeer;
        }

        @Override
        public Beer updatedById(UUID beerId, Beer beer) {
                Beer savedbeer = this.beerMap.get(beerId);
                savedbeer.setBeerName(beer.getBeerName());
                savedbeer.setBeerStyle(beer.getBeerStyle());
                savedbeer.setPrice(beer.getPrice());
                savedbeer.setQuantityOnHand(beer.getQuantityOnHand());
                savedbeer.setUpc(beer.getUpc());
                savedbeer.setUpdateDate(LocalDateTime.now());
                log.info("Updated beer : {}", savedbeer);
                return savedbeer;
        }

        @Override
        public void deletedById(UUID beerId) {
                log.info("deleteing object :{}", this.beerMap.get(beerId));
                this.beerMap.remove(beerId);
        }

}
