package com.prateek.webclient.client;

import java.util.Map;

import reactor.core.publisher.Flux;

public interface BeerClient {
    Flux<String> listBeer();

    Flux<Map> listBeerMap();
}
