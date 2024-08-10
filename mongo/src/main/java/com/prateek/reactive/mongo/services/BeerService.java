package com.prateek.reactive.mongo.services;

import com.prateek.reactive.mongo.domain.BeerDTO;

import reactor.core.publisher.Mono;

public interface BeerService {
    Mono<BeerDTO> saveBeer(BeerDTO beerDTO);

    Mono<BeerDTO> findById(String beerId);
}
