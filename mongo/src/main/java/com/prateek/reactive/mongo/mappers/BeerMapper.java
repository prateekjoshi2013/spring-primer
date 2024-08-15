package com.prateek.reactive.mongo.mappers;

import org.mapstruct.Mapper;

import com.prateek.reactive.mongo.domain.BeerDTO;
import com.prateek.reactive.mongo.model.Beer;

@Mapper
public interface BeerMapper {
    BeerDTO beerToBeerDTO(Beer beer);

    Beer beerDTOtoBeer(BeerDTO beerDTO);
}
