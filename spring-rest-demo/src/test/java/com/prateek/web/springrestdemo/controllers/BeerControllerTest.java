package com.prateek.web.springrestdemo.controllers;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prateek.web.springrestdemo.model.Beer;
import com.prateek.web.springrestdemo.model.BeerStyle;
import com.prateek.web.springrestdemo.services.BeerService;

import lombok.SneakyThrows;

@WebMvcTest(BeerController.class)
public class BeerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    BeerService beerService;

    List<Beer> mockedBeers = Stream.of(
            Beer.builder()
                    .id(UUID.randomUUID())
                    .beerName("indian pale ale")
                    .price(BigDecimal.valueOf(12.023))
                    .beerStyle(BeerStyle.PALE_ALE)
                    .quantityOnHand(12)
                    .upc("upc")
                    .version(2)
                    .createdDate(LocalDateTime.now())
                    .updateDate(LocalDateTime.now())
                    .build(),
            Beer.builder()
                    .id(UUID.randomUUID())
                    .beerName("Fosters")
                    .price(BigDecimal.valueOf(10.023))
                    .beerStyle(BeerStyle.PILSNER)
                    .quantityOnHand(10)
                    .upc("upc")
                    .version(2)
                    .createdDate(LocalDateTime.now())
                    .updateDate(LocalDateTime.now())
                    .build(),
            Beer.builder()
                    .id(UUID.randomUUID())
                    .beerName("Kingfisher")
                    .price(BigDecimal.valueOf(10.023))
                    .beerStyle(BeerStyle.STOUT)
                    .quantityOnHand(10)
                    .upc("upc")
                    .version(2)
                    .createdDate(LocalDateTime.now())
                    .updateDate(LocalDateTime.now())
                    .build())
            .collect(Collectors.toList());

    @Test
    @SneakyThrows
    void testCreateNewBearJsonString() {
        System.out.println(objectMapper.writeValueAsString(mockedBeers.get(0)));
    }

    @SneakyThrows
    @Test
    void testCreateBeer() {

        given(beerService.saveNewBeer(any(Beer.class))).willReturn(mockedBeers.get(0));

        mockMvc.perform(
                post("/api/v1/beer")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockedBeers.get(1)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().exists("location"));

    }

    @SneakyThrows
    @Test
    void testListBeers() {
        given(beerService.listBeers()).willReturn(mockedBeers);

        mockMvc.perform(get("/api/v1/beer")
                .accept(MediaType.APPLICATION_JSON))
                // Assert
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // jsonpath documentation : https://github.com/json-path/JsonPath
                .andExpect(jsonPath("$.length()", is(3)));
    }

    @SneakyThrows
    @Test
    void testGetBeerById() {

        // Get Mocked Beer
        Beer beer = mockedBeers.get(0);
        // Set up behaviours and mocks
        given(beerService.getBeerById(any(UUID.class))).willReturn(beer);

        // Act
        mockMvc.perform(get("/api/v1/beer/" + beer.getId())
                .accept(MediaType.APPLICATION_JSON))
                // Assert
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // jsonpath documentation : https://github.com/json-path/JsonPath
                .andExpect(jsonPath("$.id", is(beer.getId().toString())))
                .andExpect(jsonPath("$.beerName", is(beer.getBeerName())));

    }
}
