package com.prateek.web.springrestdemo.controllers;

import java.util.List;
import java.util.UUID;

import com.prateek.web.springrestdemo.model.Beer;
import com.prateek.web.springrestdemo.services.BeerService;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

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

    @PostMapping()
    public ResponseEntity<Beer> postBeer(@RequestBody Beer beer) {
        Beer savedBeer = this.beerServiceImpl.saveNewBeer(beer);
        HttpHeaders headers = new HttpHeaders();
        headers.add("location", "/api/v1/beer/" + savedBeer.getId());
        return new ResponseEntity<Beer>(savedBeer, headers, HttpStatus.CREATED);
    }

    @PutMapping("/{beerId}")
    public ResponseEntity<Beer> putMethodName(@PathVariable UUID beerId, @RequestBody Beer beer) {
        Beer updatedBeer = this.beerServiceImpl.updatedById(beerId, beer);
        HttpHeaders headers = new HttpHeaders();
        headers.add("location", "/api/v1/beer/" + updatedBeer.getId());
        return new ResponseEntity<Beer>(updatedBeer, headers, HttpStatus.ACCEPTED);
    }

}
