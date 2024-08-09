package com.prateek.reactive.r2dbcapp.controllers;

import java.math.BigDecimal;

import javax.print.attribute.standard.Media;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;

import com.prateek.reactive.r2dbcapp.model.BeerDTO;

import reactor.core.publisher.Mono;

// Test do not support @Transactional so we need to run the tests in a particular order since it shares the context
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@AutoConfigureWebTestClient
public class BeerControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @Test
    @Order(1)
    void testListBeers() {
        webTestClient
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
                .post().uri(BeerController.BEER_PATH)
                .body(Mono.just(getTestBeer()), BeerDTO.class)
                .header("Content-type", MediaType.APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().location("http://localhost:8080/api/v2/beer/4");
    }

    @Test
    @Order(3)
    void testUpdateBeer() {
        BeerDTO testBeer = getTestBeer();
        testBeer.setBeerName("King Fisher");
        webTestClient
                .put().uri(BeerController.BEER_PATH_ID, 4)
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
                .delete().uri(BeerController.BEER_PATH_ID, 4)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    @Order(6)
    void testGetBeerById() {
        webTestClient
                .get().uri(BeerController.BEER_PATH_ID, 1)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-type", MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.id").isEqualTo(1)
                .jsonPath("$.beerName").isEqualTo("indian pale ale");

    }

    @Test
    @Order(7)
    void testGetBeerByIdException() {

         webTestClient
                .get().uri(BeerController.BEER_PATH_ID, 999)
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().valueEquals("Content-type", MediaType.APPLICATION_JSON_VALUE)
                .expectBody()
                .jsonPath("$.error").isEqualTo("Beer not found")
                .jsonPath("$.status").isEqualTo("NOT_FOUND");

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
                .post().uri(BeerController.BEER_PATH)
                .body(Mono.just(testBeer), BeerDTO.class)
                .header("Content-type", MediaType.APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus().isBadRequest();
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
