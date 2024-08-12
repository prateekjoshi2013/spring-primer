package com.prateek.reactive.mongo.routers;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.prateek.reactive.mongo.handlers.BeerHandler;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class BeerRouter {
    public static final String BEER_PATH = "/api/beer";
    public static final String BEER_PATH_ID = BEER_PATH + "/{beerId}";

    private final BeerHandler beerHandler;

    @Bean
    public RouterFunction<ServerResponse> beerRoutes() {
        return route()
                .GET(BEER_PATH, accept(APPLICATION_JSON), beerHandler::listBeers)
                .GET(BEER_PATH_ID, accept(APPLICATION_JSON), beerHandler::getBeerById)
                .build();
    }

}
