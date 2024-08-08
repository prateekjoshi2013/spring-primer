package com.prateek.reactive.r2dbcapp.services;

import com.prateek.reactive.r2dbcapp.model.BeerDTO;

import reactor.core.publisher.Flux;

public interface BeerService {
    Flux<BeerDTO> listBeers();
}
