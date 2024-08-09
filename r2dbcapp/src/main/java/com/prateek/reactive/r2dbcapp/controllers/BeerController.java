package com.prateek.reactive.r2dbcapp.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import com.prateek.reactive.r2dbcapp.model.BeerDTO;
import com.prateek.reactive.r2dbcapp.services.BeerService;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@AllArgsConstructor
public class BeerController {

    public static final String BEER_PATH = "/api/v2/beer";
    public static final String BEER_PATH_ID = "/api/v2/beer/{beerId}";
    private final BeerService beerService;

    @GetMapping(BEER_PATH)
    public Flux<BeerDTO> listBeers() {
        return beerService.listBeers();
    }

    @GetMapping(BEER_PATH_ID)
    public Mono<BeerDTO> getBeerById(@PathVariable("beerId") Integer beerId) {
        return beerService.getBeerById(beerId)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Beer not found")));
    }

    @PostMapping(BEER_PATH)
    public Mono<ResponseEntity<Void>> createBeer(@Validated @RequestBody BeerDTO beerDTO) {

        return beerService.createBeer(beerDTO).map(savedBeer -> {
            return ResponseEntity
                    .created(
                            UriComponentsBuilder
                                    .fromHttpUrl("http://localhost:8080" + BEER_PATH + "/" + savedBeer.getId())
                                    .build().toUri())
                    .build();
        });
    }

    @PutMapping(BEER_PATH_ID)
    public Mono<ResponseEntity<Void>> updateBeer(@PathVariable("beerId") Integer id,
            @Validated @RequestBody BeerDTO beerDTO) {
        return beerService.updateBeer(id, beerDTO)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Beer not found")))
                .map(updatedBeerDTO -> {
                    return ResponseEntity.ok().build();
                });
    }

    @DeleteMapping(BEER_PATH_ID)
    public Mono<ResponseEntity<Void>> deleteBeer(@PathVariable("beerId") Integer id) {
        return beerService.deleteBeer(id)
                .switchIfEmpty(
                        Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Beer not found")))
                .then(Mono.fromCallable(() -> ResponseEntity.noContent().build()));
    }

}
