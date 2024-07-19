package com.prateek.web.springrestdemo.controllers;

import java.util.List;
import java.util.UUID;

import com.prateek.web.springrestdemo.model.Beer;
import com.prateek.web.springrestdemo.services.BeerService;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class BeerController {

    private final BeerService beerServiceImpl;

    
    @RequestMapping("/api/v1/beer")
    public List<Beer> listBeers() {
        return beerServiceImpl.listBeers();
    }

    public Beer getBeerById(UUID id) {
        return beerServiceImpl.getBeerById(id);
    }

}
