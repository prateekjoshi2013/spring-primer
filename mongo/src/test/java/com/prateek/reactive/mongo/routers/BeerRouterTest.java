package com.prateek.reactive.mongo.routers;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

@ActiveProfiles("default")
@SpringBootTest
@AutoConfigureWebTestClient
public class BeerRouterTest {
    @Autowired
    WebTestClient webTestClient;

    @Test
    void testBeerRoutes() {
        webTestClient.get().uri(BeerRouter.BEER_PATH).exchange().expectStatus().isOk()
                .expectHeader().valueEquals("Content-type", "application/json")
                .expectBody().jsonPath("$.size()", hasSize(greaterThan(1)));
    }
}
