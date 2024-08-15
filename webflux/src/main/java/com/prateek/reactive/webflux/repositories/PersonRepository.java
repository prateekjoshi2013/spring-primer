package com.prateek.reactive.webflux.repositories;

import com.prateek.reactive.webflux.domain.Person;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PersonRepository {
    Mono<Person> getById(Integer id);

    Flux<Person> findAll();
}
