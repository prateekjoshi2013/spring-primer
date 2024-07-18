package com.prateek.web.springrestdemo.controllers;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.prateek.web.springrestdemo.model.Beer;

@SpringBootTest
public class BeerControllerTest {
    @Autowired
    private BeerController beerController;

    @Test
    void testGetBeerById() {
        Beer beer = beerController.getBeerById(UUID.randomUUID());
        System.out.println(beer);
    }
}
