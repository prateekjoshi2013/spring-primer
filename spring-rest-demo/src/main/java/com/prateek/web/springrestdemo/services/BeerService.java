package com.prateek.web.springrestdemo.services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;

import com.prateek.web.springrestdemo.model.BeerDTO;
import com.prateek.web.springrestdemo.model.BeerStyle;

public interface BeerService {
    Page<BeerDTO> listBeers(String beerName, BeerStyle beerStyle, Boolean showInvetory, Integer pageNumber,
            Integer pageSize);

    Optional<BeerDTO> getBeerById(UUID id);

    BeerDTO saveNewBeer(BeerDTO beer);

    Optional<BeerDTO> updatedById(UUID beerId, BeerDTO beer);

    void deletedById(UUID beerId);
}
