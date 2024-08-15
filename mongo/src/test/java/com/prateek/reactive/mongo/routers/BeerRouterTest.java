package com.prateek.reactive.mongo.routers;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockOAuth2Login;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.util.UriComponentsBuilder;

import com.prateek.reactive.mongo.domain.BeerDTO;

import reactor.core.publisher.Mono;

@ActiveProfiles("default")
@SpringBootTest
@AutoConfigureWebTestClient
public class BeerRouterTest {
    @Autowired
    WebTestClient webTestClient;

    @Test
    void testListBeerRoute() {
        webTestClient
                .mutateWith(mockOAuth2Login())
                .get().uri(BeerRouter.BEER_PATH).exchange().expectStatus().isOk()
                .expectHeader().valueEquals("Content-type", "application/json")
                .expectBody().jsonPath("$.size()", hasSize(greaterThan(1)));
    }

    @Test
    void testListBeerRouteByBeerStyle() {
        String BEER_STYLE = "PALE_ALE";
        webTestClient.mutateWith(mockOAuth2Login()).get().uri(UriComponentsBuilder.fromPath(BeerRouter.BEER_PATH)
                .queryParam("beerStyle", BEER_STYLE).build().toUri()).exchange().expectStatus().isOk()
                .expectHeader().valueEquals("Content-type", "application/json")
                .expectBody().jsonPath("$.size()", hasSize(greaterThan(0)));
    }

    @Test
    void testGetBeerByIdRoute() {
        BeerDTO savedBeer = getSavedTestBeer();
        webTestClient
                .mutateWith(mockOAuth2Login())
                .get().uri(BeerRouter.BEER_PATH_ID, savedBeer.getId()).exchange().expectStatus().isOk()
                .expectHeader().valueEquals("Content-type", "application/json")
                .expectBody().jsonPath("$.size()", hasSize(greaterThan(1)));
    }

    @Test
    void testGetBeerByIdException() {
        webTestClient
                .mutateWith(mockOAuth2Login())
                .get().uri(BeerRouter.BEER_PATH_ID, "dummy").exchange()
                .expectHeader().valueEquals("Content-type", "application/json")
                .expectStatus().isNotFound();
    }

    @Test
    void testCreateBeerRoute() {
        webTestClient
                .mutateWith(mockOAuth2Login())
                .post().uri(BeerRouter.BEER_PATH)
                .body(Mono.just(getTestBeer()), BeerDTO.class)
                .header("Content-Type", "application/json")
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().exists("location");
    }

    @Test
    void testCreateBeerRouteValidationException() {
        BeerDTO testBeer = getTestBeer();
        testBeer.setBeerName("");
        webTestClient
                .mutateWith(mockOAuth2Login())
                .post().uri(BeerRouter.BEER_PATH)
                .body(Mono.just(testBeer), BeerDTO.class)
                .header("Content-Type", "application/json")
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void testUpdateBeerRoute() {
        BeerDTO savedBeer = getSavedTestBeer();
        webTestClient
                .mutateWith(mockOAuth2Login())
                .put().uri(BeerRouter.BEER_PATH_ID, savedBeer.getId())
                .body(Mono.just(getTestBeer()).map(beerDTO -> {
                    beerDTO.setBeerName("My Updated Beer");
                    return beerDTO;
                }), BeerDTO.class)
                .header("Content-Type", "application/json")
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void testUpdateBeerRouteValidationException() {
        BeerDTO savedBeer = getSavedTestBeer();
        webTestClient
                .mutateWith(mockOAuth2Login())  
                .put().uri(BeerRouter.BEER_PATH_ID, savedBeer.getId())
                .body(Mono.just(getTestBeer()).map(beerDTO -> {
                    beerDTO.setBeerName("");
                    return beerDTO;
                }), BeerDTO.class)
                .header("Content-Type", "application/json")
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void testUpdateBeerByIdException() {
        webTestClient
                .mutateWith(mockOAuth2Login())
                .put().uri(BeerRouter.BEER_PATH_ID, "dummy")
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
        webTestClient
                .mutateWith(mockOAuth2Login())  
                .delete().uri(BeerRouter.BEER_PATH_ID, savedBeer.getId())
                .header("Content-Type", "application/json")
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void testDeleteBeerExceptionRoute() {
        webTestClient
                .mutateWith(mockOAuth2Login())
                .delete().uri(BeerRouter.BEER_PATH_ID, "dummy")
                .header("Content-Type", "application/json")
                .exchange()
                .expectStatus().isNotFound();
    }

    public BeerDTO getSavedTestBeer() {
        FluxExchangeResult<BeerDTO> beerDTOFluxExchangeResult = webTestClient
                .mutateWith(mockOAuth2Login())
                .post().uri(BeerRouter.BEER_PATH)
                .body(Mono.just(getTestBeer()), BeerDTO.class)
                .header("Content-Type", "application/json")
                .exchange()
                .returnResult(BeerDTO.class);

        List<String> location = beerDTOFluxExchangeResult.getResponseHeaders().get("Location");

        return webTestClient
                .mutateWith(mockOAuth2Login())
                .get().uri(BeerRouter.BEER_PATH)
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
