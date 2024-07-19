package com.prateek.web.springrestdemo.controllers;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.prateek.web.springrestdemo.model.Beer;
import com.prateek.web.springrestdemo.model.BeerStyle;
import com.prateek.web.springrestdemo.services.BeerService;

import lombok.SneakyThrows;

@WebMvcTest(BeerController.class)
public class BeerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    BeerService beerService;

    @SneakyThrows
    @Test
    void testGetBeerById() {

        // Create Mock Beer
        UUID beerId = UUID.randomUUID();
        Beer beer = Beer.builder()
                .beerName("my-beer")
                .beerStyle(BeerStyle.ALE)
                .id(beerId)
                .version(1)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .price(BigDecimal.valueOf(23.98))
                .quantityOnHand(23)
                .upc("upc")
                .build();

        // Set up behaviours and mocks
        given(beerService.getBeerById(any(UUID.class))).willReturn(beer);

        // Act
        mockMvc.perform(get("/api/v1/beer/" + beerId)
                .accept(MediaType.APPLICATION_JSON))
                // Assert
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(beerId.toString())))
                .andExpect(jsonPath("$.beerName", is(beer.getBeerName())));

    }
}
