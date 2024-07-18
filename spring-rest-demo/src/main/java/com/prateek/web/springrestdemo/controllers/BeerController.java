package com.prateek.web.springrestdemo.controllers;

import java.util.UUID;

import org.springframework.stereotype.Controller;

import com.prateek.web.springrestdemo.model.Beer;
import com.prateek.web.springrestdemo.services.BeerService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
public class BeerController {

    private final BeerService beerServiceImpl;

    public Beer getBeerById(UUID id) {
        return beerServiceImpl.getBeerById(id);
    }

}
