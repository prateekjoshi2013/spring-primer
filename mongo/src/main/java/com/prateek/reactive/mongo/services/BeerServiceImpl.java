package com.prateek.reactive.mongo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.server.ResponseStatusException;

import com.prateek.reactive.mongo.domain.BeerDTO;
import com.prateek.reactive.mongo.mappers.BeerMapper;
import com.prateek.reactive.mongo.repositories.BeerRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
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
        return beerRepository.findById(beerId)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Beer Not Found")))
                .map(beerMapper::beerToBeerDTO);
    }

    @Override
    public Mono<BeerDTO> saveBeer(BeerDTO beerDTO) {
        return beerRepository
                .save(beerMapper.beerDTOtoBeer(beerDTO))
                .map(beerMapper::beerToBeerDTO);
    }

    @Override
    public Flux<BeerDTO> listBeers() {
        return beerRepository
                .findAll()
                .map(beerMapper::beerToBeerDTO);
    }

    @Override
    public Mono<BeerDTO> updateBeer(String beerId, BeerDTO beerDTO) {
        return beerRepository
                .findById(beerId)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Beer not found")))
                .flatMap(savedBeer -> {
                    savedBeer.setBeerName(beerDTO.getBeerName());
                    savedBeer.setBeerStyle(beerDTO.getBeerStyle());
                    savedBeer.setPrice(beerDTO.getPrice());
                    savedBeer.setQuantityOnHand(beerDTO.getQuantityOnHand());
                    savedBeer.setUpc(beerDTO.getUpc());
                    return beerRepository.save(savedBeer).map(beerMapper::beerToBeerDTO);
                });
    }

    @Override
    public Mono<Void> deleteBeer(String beerId) {
        return beerRepository
                .findById(beerId)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Beer not found")))
                .then(Mono.empty());
    }

    @Override
    public Mono<BeerDTO> getBeerByName(String beerName) {
        return beerRepository
                .findFirstByAllIgnoreCaseBeerName(beerName)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Beer not found")))
                .map(beerMapper::beerToBeerDTO);
    }

    @Override
    public Flux<BeerDTO> getBeersByBeerStyle(String beerStyle) {
        return beerRepository
                .findByBeerStyle(beerStyle)
                .map(beerMapper::beerToBeerDTO);
    }

}
