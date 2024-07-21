package com.prateek.web.springrestdemo.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;

import com.prateek.web.springrestdemo.domain.entities.Beer;
import com.prateek.web.springrestdemo.domain.exceptions.NoBeerFoundException;
import com.prateek.web.springrestdemo.mappers.BeerMapper;
import com.prateek.web.springrestdemo.model.BeerDTO;
import com.prateek.web.springrestdemo.repositories.BeerRepository;

import jakarta.transaction.Transactional;

@SpringBootTest
public class BeerControllerIT {

    @Autowired
    BeerController beerController;

    @Autowired
    BeerRepository beerRepository;

    @Autowired
    BeerMapper beerMapper;

    @Transactional
    @Rollback
    @Test
    void testUpdateNonExistingBeer() {
        UUID randomUUID = UUID.randomUUID();
        Beer beer = beerRepository.findAll().get(0);
        BeerDTO beerDto = beerMapper.beerToBeerDto(beer);
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
        BeerDTO beerDto = beerMapper.beerToBeerDto(beer);
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

        BeerDTO beerDto = beerController.listBeers().get(0);
        BeerDTO beer = beerController.getBeerById(beerDto.getId());
        assertEquals(beerDto.getId(), beer.getId());
    }

    @Test
    void testListBeers() {
        List<BeerDTO> beers = beerController.listBeers();
        assertThat(beers.size()).isEqualTo(3);
    }

    @Rollback
    @Transactional
    @Test
    void testEmptyList() {
        beerRepository.deleteAll();
        List<BeerDTO> dtos = beerController.listBeers();
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
