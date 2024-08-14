package com.prateek.webclient.client;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Flux;

@Service
public class BeerClientImpl implements BeerClient {

    private final WebClient webClient;

    public BeerClientImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8080").build();
    }

    @Override
    public Flux<String> listBeer() {
        return webClient.get().uri("/api/beer", String.class)
                .retrieve().bodyToFlux(String.class);
    }

}
