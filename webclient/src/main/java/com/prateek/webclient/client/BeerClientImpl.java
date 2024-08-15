package com.prateek.webclient.client;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.prateek.webclient.model.BeerDTO;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class BeerClientImpl implements BeerClient {

    private static final String BEER_PATH = "/api/beer";
    private static final String BEER_PATH_ID = "/api/beer/{beerId}";
    private final WebClient webClient;

    @Value("${webclient.rooturl}")
    private String rootUrl;

    public BeerClientImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    @Override
    public Flux<String> listBeer() {
        return webClient.get().uri(BEER_PATH)
                .retrieve().bodyToFlux(String.class);
    }

    @Override
    public Flux<Map> listBeerMap() {
        return webClient.get().uri(BEER_PATH)
                .retrieve().bodyToFlux(Map.class);
    }

    @Override
    public Flux<JsonNode> listBeerJsonNode() {
        return webClient.get().uri(BEER_PATH)
                .retrieve().bodyToFlux(JsonNode.class);
    }

    @Override
    public Flux<BeerDTO> listBeerDto() {
        return webClient.get().uri(BEER_PATH)
                .retrieve().bodyToFlux(BeerDTO.class);
    }

    @Override
    public Mono<BeerDTO> getByBeerId(String beerId) {
        return webClient.get().uri(BEER_PATH_ID, beerId)
                .retrieve().bodyToMono(BeerDTO.class);
    }

    @Override
    public Flux<BeerDTO> getBeersByStyle(String beerStyle) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path(BEER_PATH)
                        .queryParam("beerStyle", beerStyle).build())
                .retrieve().bodyToFlux(BeerDTO.class);
    }

    @Override
    public Mono<BeerDTO> createBeer(BeerDTO beerDTO) {
        return webClient.post().uri(BEER_PATH)
                .bodyValue(beerDTO)
                .retrieve()
                .toBodilessEntity()
                .doOnNext(body -> {
                    System.out.println("-->" + body);
                    System.out.println();
                })
                .flatMap(body -> webClient.get()
                        .uri(rootUrl + body.getHeaders().getLocation().toString())
                        .retrieve().bodyToMono(BeerDTO.class))
                .doOnNext(beer -> {
                    System.out.println(beer);
                });
    }

}
