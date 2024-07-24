package com.prateek.web.springrestdemo.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.prateek.web.springrestdemo.model.BeerDTO;
import com.prateek.web.springrestdemo.model.BeerStyle;

public interface BeerService {
    List<BeerDTO> listBeers(String beerName, BeerStyle beerStyle, Boolean showInvetory);

    Optional<BeerDTO> getBeerById(UUID id);

    BeerDTO saveNewBeer(BeerDTO beer);

    Optional<BeerDTO> updatedById(UUID beerId, BeerDTO beer);

    void deletedById(UUID beerId);
}
