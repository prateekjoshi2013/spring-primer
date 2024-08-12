package com.prateek.reactive.r2dbcapp.repositories;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.prateek.reactive.r2dbcapp.domain.Beer;

public interface BeerRepository extends ReactiveCrudRepository<Beer, Integer> {

}
