package com.prateek.reactive.r2dbcapp.services;

import org.springframework.stereotype.Service;

import com.prateek.reactive.r2dbcapp.mappers.BeerMapper;
import com.prateek.reactive.r2dbcapp.model.BeerDTO;
import com.prateek.reactive.r2dbcapp.repositories.BeerRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class BeerServiceImpl implements BeerService {

    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;

    @Override
    public Flux<BeerDTO> listBeers() {
        return beerRepository.findAll().map(beerMapper::beerToBeerDTO);
    }

}
