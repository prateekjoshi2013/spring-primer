package com.prateek.reactive.mongo.handlers;

import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebInputException;
import org.springframework.web.util.UriComponentsBuilder;

import com.prateek.reactive.mongo.domain.BeerDTO;
import com.prateek.reactive.mongo.routers.BeerRouter;
import com.prateek.reactive.mongo.services.BeerService;
import org.springframework.validation.Errors;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class BeerHandler {
    private final BeerService beerService;
    private final Validator validator;

    private void validate(BeerDTO beerDTO){
        Errors errors=new BeanPropertyBindingResult(beerDTO, "beerDto");
        validator.validate(beerDTO, errors);
        if (errors.hasErrors()){
                throw new ServerWebInputException(errors.toString());
        }
    }

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
                .doOnNext(this::validate)
                .flatMap(beerDTO -> beerService.saveBeer(beerDTO))
                .flatMap(beer -> ServerResponse
                        .created(
                                UriComponentsBuilder
                                        .fromPath(BeerRouter.BEER_PATH_ID)
                                        .build(beer.getId()))
                        .build());

    }

    public Mono<ServerResponse> updateBeer(ServerRequest request){
       return  request.bodyToMono(BeerDTO.class)
                .doOnNext(this::validate)
                .flatMap(beer->beerService.updateBeer(request.pathVariable("beerId"), beer))
                .flatMap(updatedBeer->ServerResponse.noContent().build());
               
    }

    public Mono<ServerResponse> deleteBeer(ServerRequest request) {
       return  beerService.deleteBeer(request.pathVariable("beerId"))
        .then(ServerResponse.noContent().build());
    }
}
