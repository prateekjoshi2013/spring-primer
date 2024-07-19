package com.prateek.web.springrestdemo.controllers;

import java.util.List;
import java.util.UUID;

import com.prateek.web.springrestdemo.model.Beer;
import com.prateek.web.springrestdemo.services.BeerService;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/beer")
public class BeerController {

    private final BeerService beerServiceImpl;

    @RequestMapping("")
    public List<Beer> listBeers() {
        return beerServiceImpl.listBeers();
    }

    @GetMapping("{beerId}")
    public Beer getBeerById(@PathVariable("beerId") UUID beerId) {
        return beerServiceImpl.getBeerById(beerId);
    }

}
