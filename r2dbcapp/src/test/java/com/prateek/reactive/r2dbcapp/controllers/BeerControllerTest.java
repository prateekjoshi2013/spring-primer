package com.prateek.reactive.r2dbcapp.controllers;

import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockOAuth2Login;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.prateek.reactive.r2dbcapp.domain.Beer;
import com.prateek.reactive.r2dbcapp.model.BeerDTO;
import com.prateek.reactive.r2dbcapp.repositories.BeerRepository;

import reactor.core.publisher.Mono;

// Test do not support @Transactional so we need to run the tests in a particular order since it shares the context
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@AutoConfigureWebTestClient
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BeerControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    BeerRepository beerRepository;

    private Integer latestId;
    private Integer firstId;

    @BeforeAll
    void setUp() {
        List<Beer> block = beerRepository.findAll().collectList().block();
        block.sort((blk1, blk2) -> blk2.getId() - blk1.getId());
        latestId = block.get(0).getId();
        firstId = block.get(block.size() - 1).getId();
    }

    @Test
    @Order(1)
    void testListBeers() {
        webTestClient
                .mutateWith(mockOAuth2Login())
                .get().uri(BeerController.BEER_PATH)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-type", MediaType.APPLICATION_JSON_VALUE)
                .expectBody().jsonPath("$.size()").isEqualTo(3);

    }

    @Test
    @Order(2)
    void testCreateBeer() {
        webTestClient
                .mutateWith(mockOAuth2Login())
                .post().uri(BeerController.BEER_PATH)
                .body(Mono.just(getTestBeer()), BeerDTO.class)
                .header("Content-type", MediaType.APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().location("http://localhost:8080/api/v2/beer/" + (latestId + 1));
    }

    @Test
    @Order(3)
    void testUpdateBeer() {
        BeerDTO testBeer = getTestBeer();
        testBeer.setBeerName("King Fisher");
        webTestClient
                .mutateWith(mockOAuth2Login())
                .put().uri(BeerController.BEER_PATH_ID, (latestId + 1))
                .body(Mono.just(testBeer), BeerDTO.class)
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    @Order(4)
    void testUpdateBeerBadRequest() {
        BeerDTO testBeer = getTestBeer();
        testBeer.setBeerName("");

        webTestClient
                .mutateWith(mockOAuth2Login())
                .put().uri(BeerController.BEER_PATH_ID, 4)
                .body(Mono.just(testBeer), BeerDTO.class)
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @Order(5)
    void testDeleteBeer() {
        webTestClient
                .mutateWith(mockOAuth2Login())
                .delete().uri(BeerController.BEER_PATH_ID, latestId)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    @Order(6)
    void testGetBeerById() {
        webTestClient
                .mutateWith(mockOAuth2Login())
                .get().uri(BeerController.BEER_PATH_ID, firstId)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-type", MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.id").isEqualTo(firstId);

    }

    @Test
    @Order(7)
    void testGetBeerByIdException() {

        webTestClient
                .mutateWith(mockOAuth2Login())
                .get().uri(BeerController.BEER_PATH_ID, 999)
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().valueEquals("Content-type", MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.error").isEqualTo("Beer not found")
                .jsonPath("$.status").isEqualTo("404 NOT_FOUND");

        // EntityExchangeResult<String> result = webTestClient
        // .get().uri(BeerController.BEER_PATH_ID, 999)
        // .exchange()
        // .expectStatus().isNotFound()
        // .expectBody(String.class)
        // .returnResult();
        // System.out.println(result.getResponseBody());
    }

    @Test
    @Order(8)
    void testCreateBeerBadRequest() {
        BeerDTO testBeer = getTestBeer();
        testBeer.setBeerName("");
        webTestClient
                .mutateWith(mockOAuth2Login())
                .post().uri(BeerController.BEER_PATH)
                .body(Mono.just(testBeer), BeerDTO.class)
                .header("Content-type", MediaType.APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @Order(9)
    void testUpdateBeerNotFound() {
        BeerDTO testBeer = getTestBeer();
        testBeer.setBeerName("My test beer");

        webTestClient
                .mutateWith(mockOAuth2Login())
                .put().uri(BeerController.BEER_PATH_ID, 999)
                .body(Mono.just(testBeer), BeerDTO.class)
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .exchange()
                .expectBody()
                .jsonPath("$.error").isEqualTo("Beer not found")
                .jsonPath("$.status").isEqualTo("404 NOT_FOUND");
    }

    @Test
    @Order(9)
    void testDeleteBeerNotFound() {
        webTestClient
                .mutateWith(mockOAuth2Login())
                .delete().uri(BeerController.BEER_PATH_ID, 999)
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .exchange()
                .expectBody()
                .jsonPath("$.error").isEqualTo("Beer not found")
                .jsonPath("$.status").isEqualTo("404 NOT_FOUND");
    }

    BeerDTO getTestBeer() {
        return BeerDTO.builder()
                .beerName("Kingfisher")
                .beerStyle("lager")
                .price(BigDecimal.valueOf(10.5f))
                .quantityOnHand(12)
                .upc("upc")
                .build();
    }
}
