package com.prateek.reactive.r2dbcapp.services;

import org.springframework.stereotype.Service;

import com.prateek.reactive.r2dbcapp.domain.Beer;
import com.prateek.reactive.r2dbcapp.mappers.BeerMapper;
import com.prateek.reactive.r2dbcapp.model.BeerDTO;
import com.prateek.reactive.r2dbcapp.repositories.BeerRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class BeerServiceImpl implements BeerService {

    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;

    @Override
    public Flux<BeerDTO> listBeers() {
        return beerRepository.findAll().map(beerMapper::beerToBeerDTO);
    }

    @Override
    public Mono<BeerDTO> getBeerById(Integer id) {
        return beerRepository.findById(id).map(beerMapper::beerToBeerDTO);
    }

    @Override
    public Mono<BeerDTO> createBeer(BeerDTO beerDTO) {
        return beerRepository.save(beerMapper.BeerDTOtoBeer(beerDTO)).map(beerMapper::beerToBeerDTO);
    }

    @Override
    public Mono<BeerDTO> updateBeer(Integer beerId, BeerDTO beerDTO) {
        return beerRepository.findById(beerId)
                .flatMap(beer -> {
                    Beer updatedBeer = Beer.builder()
                            .id(beerId)
                            .beerName(beerDTO.getBeerName())
                            .beerStyle(beerDTO.getBeerStyle())
                            .upc(beerDTO.getUpc())
                            .price(beerDTO.getPrice())
                            .quantityOnHand(beerDTO.getQuantityOnHand())
                            .build();
                    return beerRepository.save(updatedBeer);
                }).map(beerMapper::beerToBeerDTO);
    }

}
