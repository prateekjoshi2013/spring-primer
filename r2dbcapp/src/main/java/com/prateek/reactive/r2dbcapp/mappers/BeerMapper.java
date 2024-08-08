package com.prateek.reactive.r2dbcapp.mappers;

import org.mapstruct.Mapper;

import com.prateek.reactive.r2dbcapp.domain.Beer;
import com.prateek.reactive.r2dbcapp.model.BeerDTO;

@Mapper
public interface BeerMapper {

    Beer BeerDTOtoBeer(BeerDTO beerDTO);

    BeerDTO beerToBeerDTO(Beer beer);

}
