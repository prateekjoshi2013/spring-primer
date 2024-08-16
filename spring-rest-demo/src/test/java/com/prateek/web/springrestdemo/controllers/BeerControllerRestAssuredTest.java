package com.prateek.web.springrestdemo.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.ActiveProfiles;

import com.atlassian.oai.validator.OpenApiInteractionValidator;
import com.atlassian.oai.validator.restassured.OpenApiValidationFilter;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import lombok.SneakyThrows;

import static io.restassured.RestAssured.given;

/**
 * Test the swagger generated docs agains api to ensure open api compatibility
 * - Rest Assured: to test against a running server
 */

@ActiveProfiles("test") // test profile for rest assured test specific config
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT) // random available port to run the server for test
@Import(BeerControllerRestAssuredTest.TestConfig.class) // importing security filter chain with no auth specifically for
                                                        // restassured test
@ComponentScan(basePackages = "com.prateek.web.springrestdemo") // to enable component scan for all other classes except
                                                                // SpringSpecConfig
public class BeerControllerRestAssuredTest {

    OpenApiValidationFilter filter = new OpenApiValidationFilter(OpenApiInteractionValidator
            .createForSpecificationUrl("openapi.json").build());

    @Configuration
    public static class TestConfig {
        @Bean
        @SneakyThrows
        public SecurityFilterChain filterChain(HttpSecurity http) {
            http
                    .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                            .anyRequest().permitAll()); // no auth on any request
            return http.build();
        }
    }

    @LocalServerPort
    Integer localPort; // randomly selected port

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = localPort;
    }

    @Test
    void testListBeers() {
        given().contentType(ContentType.JSON)
                .when()
                .filter(filter) // tests requests and response agains the generaed openapi spec
                .get("/api/v1/beer")
                .then()
                .assertThat().statusCode(200);
    }
}
