package com.prateek.reactive.mongo.routers;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.prateek.reactive.mongo.domain.BeerDTO;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ActiveProfiles("default")
@SpringBootTest
@AutoConfigureWebTestClient
public class BeerRouterTest {
    @Autowired
    WebTestClient webTestClient;

    @Test
    void testListBeerRoute() {
        webTestClient.get().uri(BeerRouter.BEER_PATH).exchange().expectStatus().isOk()
                .expectHeader().valueEquals("Content-type", "application/json")
                .expectBody().jsonPath("$.size()", hasSize(greaterThan(1)));
    }

    @Test
    void testGetBeerByIdRoute() {
        BeerDTO savedBeer = getSavedTestBeer();
        webTestClient.get().uri(BeerRouter.BEER_PATH_ID, savedBeer.getId()).exchange().expectStatus().isOk()
                .expectHeader().valueEquals("Content-type", "application/json")
                .expectBody().jsonPath("$.size()", hasSize(greaterThan(1)));
    }

    @Test
    void testGetBeerByIdException() {
        webTestClient.get().uri(BeerRouter.BEER_PATH_ID, "dummy").exchange()
                .expectHeader().valueEquals("Content-type", "application/json")
                .expectStatus().isNotFound();
    }

    @Test
    void testCreateBeerRoute() {
        webTestClient.post().uri(BeerRouter.BEER_PATH)
                .body(Mono.just(getTestBeer()), BeerDTO.class)
                .header("Content-Type", "application/json")
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().exists("location");
    }

    @Test
    void testUpdateBeerRoute() {
        BeerDTO savedBeer = getSavedTestBeer();
        webTestClient.put().uri(BeerRouter.BEER_PATH_ID, savedBeer.getId())
                .body(Mono.just(getTestBeer()).map(beerDTO -> {
                    beerDTO.setBeerName("My Updated Beer");
                    return beerDTO;
                }), BeerDTO.class)
                .header("Content-Type", "application/json")
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void testUpdateBeerByIdException() {
        webTestClient.put().uri(BeerRouter.BEER_PATH_ID, "dummy")
                .body(Mono.just(getTestBeer()).map(beerDTO -> {
                    beerDTO.setBeerName("My Updated Beer");
                    return beerDTO;
                }), BeerDTO.class)
                .header("Content-Type", "application/json")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testDeleteBeerRoute() {
        BeerDTO savedBeer = getSavedTestBeer();
        webTestClient.delete().uri(BeerRouter.BEER_PATH_ID, savedBeer.getId())
                .header("Content-Type", "application/json")
                .exchange()
                .expectStatus().isNoContent();
    }

    public BeerDTO getSavedTestBeer() {
        FluxExchangeResult<BeerDTO> beerDTOFluxExchangeResult = webTestClient.post().uri(BeerRouter.BEER_PATH)
                .body(Mono.just(getTestBeer()), BeerDTO.class)
                .header("Content-Type", "application/json")
                .exchange()
                .returnResult(BeerDTO.class);

        List<String> location = beerDTOFluxExchangeResult.getResponseHeaders().get("Location");

        return webTestClient.get().uri(BeerRouter.BEER_PATH)
                .exchange().returnResult(BeerDTO.class).getResponseBody().blockFirst();
    }

    public BeerDTO getTestBeer() {
        return BeerDTO.builder()
                .beerName("My Bear")
                .beerStyle("PALE_ALE")
                .price(BigDecimal.TEN)
                .quantityOnHand(10)
                .upc("upc")
                .build();
    }
}
