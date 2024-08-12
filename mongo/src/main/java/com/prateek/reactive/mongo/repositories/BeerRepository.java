package com.prateek.reactive.mongo.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.prateek.reactive.mongo.model.Beer;

import reactor.core.publisher.Mono;

public interface BeerRepository extends ReactiveMongoRepository<Beer, String> {
    Mono<Beer> findFirstByAllIgnoreCaseBeerName(String beerName);
}
