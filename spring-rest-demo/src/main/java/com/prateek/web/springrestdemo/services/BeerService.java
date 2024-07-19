package com.prateek.web.springrestdemo.services;

import java.util.List;
import java.util.UUID;

import com.prateek.web.springrestdemo.model.Beer;

public interface BeerService {
    List<Beer> listBeers();

    Beer getBeerById(UUID id);

    Beer saveNewBeer(Beer beer);

    Beer updatedById(UUID beerId, Beer beer);
}
