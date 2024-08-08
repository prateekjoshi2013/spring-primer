package com.prateek.reactive.r2dbcapp.services;

import com.prateek.reactive.r2dbcapp.model.BeerDTO;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BeerService {
    Flux<BeerDTO> listBeers();

    Mono<BeerDTO> getBeerById(Integer id);

    Mono<BeerDTO> createBeer(BeerDTO beerDTO);

    Mono<BeerDTO> updateBeer(Integer id, BeerDTO beerDTO);
}
