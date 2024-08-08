package com.prateek.reactive.r2dbcapp.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.prateek.reactive.r2dbcapp.model.BeerDTO;
import com.prateek.reactive.r2dbcapp.services.BeerService;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@AllArgsConstructor
public class BeerController {

    public final String BEER_PATH = "/api/v2/beer";
    public final String BEER_PATH_ID = "/api/v2/beer/{beerId}";
    private final BeerService beerService;

    @GetMapping(BEER_PATH)
    public Flux<BeerDTO> listBeers() {
        return beerService.listBeers();
    }

    @GetMapping(BEER_PATH_ID)
    public Mono<BeerDTO> getBeerById(@PathVariable("beerId") Integer beerId) {
        return beerService.getBeerById(beerId);
    }

}
