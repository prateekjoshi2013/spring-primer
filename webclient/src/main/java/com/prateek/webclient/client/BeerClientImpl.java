package com.prateek.webclient.client;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.prateek.webclient.model.BeerDTO;

import reactor.core.publisher.Flux;

@Service
public class BeerClientImpl implements BeerClient {

    private static final String BEER_PATH = "/api/beer";
    private final WebClient webClient;

    public BeerClientImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8080").build();
    }

    @Override
    public Flux<String> listBeer() {
        return webClient.get().uri(BEER_PATH, String.class)
                .retrieve().bodyToFlux(String.class);
    }

    @Override
    public Flux<Map> listBeerMap() {
       return webClient.get().uri(BEER_PATH, Map.class)
       .retrieve().bodyToFlux(Map.class);
    }

    @Override
    public Flux<JsonNode> listBeerJsonNode() {
        return webClient.get().uri(BEER_PATH, JsonNode.class)
        .retrieve().bodyToFlux(JsonNode.class);
    }

    @Override
    public Flux<BeerDTO> listBeerDto() {
        return webClient.get().uri(BEER_PATH,BeerDTO.class)
                .retrieve().bodyToFlux(BeerDTO.class);
    }

}
