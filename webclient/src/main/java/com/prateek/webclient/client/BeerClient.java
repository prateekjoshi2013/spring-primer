package com.prateek.webclient.client;

import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.prateek.webclient.model.BeerDTO;

import reactor.core.publisher.Flux;

public interface BeerClient {
    Flux<String> listBeer();

    Flux<Map> listBeerMap();

    Flux<JsonNode> listBeerJsonNode();

    Flux<BeerDTO> listBeerDto();
}
