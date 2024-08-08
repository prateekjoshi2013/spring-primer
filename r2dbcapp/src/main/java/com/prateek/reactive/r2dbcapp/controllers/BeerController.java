package com.prateek.reactive.r2dbcapp.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.prateek.reactive.r2dbcapp.model.BeerDTO;
import com.prateek.reactive.r2dbcapp.services.BeerService;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;

import org.springframework.web.bind.annotation.GetMapping;

@RestController
@AllArgsConstructor
public class BeerController {

    public final String BEER_PATH = "/api/v2/beer";
    private final BeerService beerService;

    @GetMapping(BEER_PATH)
    public Flux<BeerDTO> listBeers() {
        return beerService.listBeers();
    }

}
