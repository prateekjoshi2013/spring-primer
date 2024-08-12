package com.prateek.reactive.mongo.services;

import com.prateek.reactive.mongo.domain.BeerDTO;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BeerService {
    Mono<BeerDTO> saveBeer(BeerDTO beerDTO);

    Mono<BeerDTO> findById(String beerId);

    Flux<BeerDTO> listBeers();

    Mono<BeerDTO> updateBeer(String beerId, BeerDTO beer);

    Mono<Void> deleteBeer(String beerId);

    Mono<BeerDTO> getBeerByName(String beerName);

    Flux<BeerDTO> getBeersByBeerStyle(String beerStyle);
}
