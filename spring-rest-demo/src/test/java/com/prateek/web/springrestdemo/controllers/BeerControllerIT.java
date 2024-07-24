package com.prateek.web.springrestdemo.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prateek.web.springrestdemo.domain.entities.Beer;
import com.prateek.web.springrestdemo.domain.exceptions.NoBeerFoundException;
import com.prateek.web.springrestdemo.mappers.BeerMapper;
import com.prateek.web.springrestdemo.model.BeerDTO;
import com.prateek.web.springrestdemo.model.BeerStyle;
import com.prateek.web.springrestdemo.repositories.BeerRepository;

import jakarta.transaction.Transactional;
import lombok.SneakyThrows;

@SpringBootTest
public class BeerControllerIT {

    @Autowired
    BeerController beerController;

    @Autowired
    BeerRepository beerRepository;

    @Autowired
    BeerMapper beerMapperImpl;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    WebApplicationContext wac;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    @SneakyThrows
    void testListBeersByName() {
        mockMvc.perform(get(BeerController.API_V1_BEER)
                .queryParam("beerName", "IPA")).andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(336)));
    }

    @SneakyThrows
    @Test
    void testSaveBeerEndToEnd() {
        BeerDTO beer = BeerDTO.builder()
                .beerName("My Beer asdasdjlkasd laksjd alsdjla dlakdjlajdlakjdlajkdlajdlajldjaldjlajdlkj")
                .beerStyle(BeerStyle.ALE)
                .price(BigDecimal.valueOf(1.12))
                .upc("upc")
                .build();
        mockMvc.perform(
                post(BeerController.API_V1_BEER)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beer))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.length()", is(1)));
    }

    @Transactional
    @Rollback
    @Test
    void testUpdateNonExistingBeer() {
        UUID randomUUID = UUID.randomUUID();
        Beer beer = beerRepository.findAll().get(0);
        BeerDTO beerDto = beerMapperImpl.beerToBeerDto(beer);
        beerDto.setId(null);
        beerDto.setVersion(null);
        beerDto.setBeerName("updated name");
        Exception exception = assertThrows(NoBeerFoundException.class, () -> {
            beerController.putMethodName(randomUUID, beerDto);
        });
        assertEquals(exception.getMessage(), "Beer with id: " + randomUUID + " not found");
    }

    @Transactional
    @Rollback
    @Test
    void testUpdateExistingBeer() {
        Beer beer = beerRepository.findAll().get(0);
        BeerDTO beerDto = beerMapperImpl.beerToBeerDto(beer);
        beerDto.setId(null);
        beerDto.setVersion(null);
        beerDto.setBeerName("updated name");
        beerController.putMethodName(beer.getId(), beerDto);
        beerRepository.findById(beer.getId())
                .ifPresent(updatedBeer -> assertEquals(updatedBeer.getBeerName(), "updated name"));
        ;
    }

    @Test
    void testBeerIdNotFound() {
        UUID randomBeerId = UUID.randomUUID();
        Exception e = assertThrows(NoBeerFoundException.class, () -> {
            beerController.getBeerById(randomBeerId);
        });

        assertEquals(e.getMessage(), "Beer with id: " + randomBeerId.toString() + " not found");
    }

    @Test
    void testByGetId() {

        BeerDTO beerDto = beerController.listBeers(null).get(0);
        BeerDTO beer = beerController.getBeerById(beerDto.getId());
        assertEquals(beerDto.getId(), beer.getId());
    }

    @Test
    void testListBeers() {
        List<BeerDTO> beers = beerController.listBeers(null);
        assertThat(beers.size()).isEqualTo(2410);
    }

    @Rollback
    @Transactional
    @Test
    void testEmptyList() {
        beerRepository.deleteAll();
        List<BeerDTO> dtos = beerController.listBeers(null);
        assertThat(dtos.size()).isEqualTo(0);
    }

    @Rollback
    @Transactional
    @Test
    void testSaveBeer() {
        ResponseEntity<BeerDTO> responseEntity = beerController.postBeer(BeerDTO.builder().beerName("My beer").build());
        assertEquals("My beer", responseEntity.getBody().getBeerName());
    }

    @Rollback
    @Transactional
    @Test
    void testDeleteExistingBeer() {
        Beer beer = beerRepository.findAll().get(0);
        beerController.deleteBearById(beer.getId());
    }

    @Rollback
    @Transactional
    @Test
    void testDeleteNonExistingBeer() {
        UUID uuid = UUID.randomUUID();
        assertThrows(NoBeerFoundException.class, () -> {
            beerController.deleteBearById(uuid);
        });
    }

}
