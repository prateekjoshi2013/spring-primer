package com.prateek.webclient.client;

import reactor.core.publisher.Flux;


public interface BeerClient {
    Flux<String> listBeer();
}
