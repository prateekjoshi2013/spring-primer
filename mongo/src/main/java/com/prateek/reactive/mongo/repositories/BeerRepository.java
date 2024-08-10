package com.prateek.reactive.mongo.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.prateek.reactive.mongo.model.Beer;

public interface BeerRepository extends ReactiveMongoRepository<Beer, String> {

}
