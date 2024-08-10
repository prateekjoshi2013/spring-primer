package com.prateek.reactive.mongo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prateek.reactive.mongo.domain.BeerDTO;
import com.prateek.reactive.mongo.mappers.BeerMapper;
import com.prateek.reactive.mongo.repositories.BeerRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class BeerServiceImpl implements BeerService {

    @Autowired
    private final BeerMapper beerMapper;
    @Autowired
    private final BeerRepository beerRepository;

    @Override
    public Mono<BeerDTO> findById(String beerId) {
        return beerRepository.findById(beerId).map(beerMapper::beerToBeerDTO);
    }

    @Override
    public Mono<BeerDTO> saveBeer(BeerDTO beerDTO) {
        return beerRepository
                .save(beerMapper.beerDTOtoBeer(beerDTO))
                .map(beerMapper::beerToBeerDTO);
    }

}
