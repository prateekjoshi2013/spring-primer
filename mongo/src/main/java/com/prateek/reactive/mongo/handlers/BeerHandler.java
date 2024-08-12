package com.prateek.reactive.mongo.handlers;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.prateek.reactive.mongo.domain.BeerDTO;
import com.prateek.reactive.mongo.services.BeerService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class BeerHandler {
    private final BeerService beerService;

    public Mono<ServerResponse> listBeers(ServerRequest request) {
        return ServerResponse
                .ok()
                .body(beerService.listBeers(), BeerDTO.class);
    }

    public Mono<ServerResponse> getBeerById(ServerRequest request) {
        return ServerResponse.ok()
                .body(beerService.findById(request.pathVariable("beerId")), BeerDTO.class);
    }
    
}
