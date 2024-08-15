package com.prateek.webclient.client;

import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.prateek.webclient.model.BeerDTO;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BeerClient {
    Flux<String> listBeer();

    Flux<Map> listBeerMap();

    Flux<JsonNode> listBeerJsonNode();

    Flux<BeerDTO> listBeerDto();

    Mono<BeerDTO> getByBeerId(String beerId);

    Flux<BeerDTO> getBeersByStyle(String beerStyle);

    Mono<BeerDTO> createBeer(BeerDTO beerDTO);
}
