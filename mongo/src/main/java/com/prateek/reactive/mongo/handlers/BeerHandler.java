package com.prateek.reactive.mongo.handlers;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.util.UriComponentsBuilder;

import com.prateek.reactive.mongo.domain.BeerDTO;
import com.prateek.reactive.mongo.routers.BeerRouter;
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

    public Mono<ServerResponse> createBeer(ServerRequest request) {
        return request.bodyToMono(BeerDTO.class)
                .flatMap(beerDTO -> beerService.saveBeer(beerDTO))
                .flatMap(beer -> ServerResponse
                        .created(
                                UriComponentsBuilder
                                        .fromPath(BeerRouter.BEER_PATH_ID)
                                        .build(beer.getId()))
                        .build());

    }

    public Mono<ServerResponse> updateBeer(ServerRequest request){
        return beerService.findById(request.pathVariable("beerId"))
                .flatMap(foundBeer->{
                        return request.bodyToMono(BeerDTO.class).map(beerDTO->{
                                foundBeer.setBeerName(beerDTO.getBeerName());
                                foundBeer.setBeerStyle(beerDTO.getBeerStyle());
                                foundBeer.setPrice(beerDTO.getPrice());
                                foundBeer.setQuantityOnHand(beerDTO.getQuantityOnHand());
                                foundBeer.setUpc(beerDTO.getUpc());
                                return foundBeer;
                        });
                })
                .flatMap(beerToBeUpdated-> beerService.saveBeer(beerToBeUpdated))
                .flatMap(updatedBeer->ServerResponse.noContent().build());
    }

}
