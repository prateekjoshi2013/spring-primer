package com.prateek.web.springrestdemo.controllers;

import java.util.List;
import java.util.UUID;

import com.prateek.web.springrestdemo.model.BeerDTO;
import com.prateek.web.springrestdemo.services.BeerService;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@AllArgsConstructor
@RestController
public class BeerController {

    public static final String API_V1_BEER = "/api/v1/beer";
    public static final String CUSTOMER_ID = "/{beerId}";
    public static final String API_V1_BEER_ID_PATH = API_V1_BEER + CUSTOMER_ID;
    private final BeerService beerServiceImpl;

    @RequestMapping(API_V1_BEER)
    public List<BeerDTO> listBeers() {
        return beerServiceImpl.listBeers();
    }

    @GetMapping(API_V1_BEER_ID_PATH)
    public BeerDTO getBeerById(@PathVariable("beerId") UUID beerId) {
        return beerServiceImpl.getBeerById(beerId).get();
    }

    @PostMapping(API_V1_BEER)
    public ResponseEntity<BeerDTO> postBeer(@RequestBody BeerDTO beer) {
        BeerDTO savedBeer = this.beerServiceImpl.saveNewBeer(beer);
        HttpHeaders headers = new HttpHeaders();
        headers.add("location", API_V1_BEER + "/" + savedBeer.getId());
        return new ResponseEntity<BeerDTO>(savedBeer, headers, HttpStatus.CREATED);
    }

    @PutMapping(API_V1_BEER_ID_PATH)
    public ResponseEntity<BeerDTO> putMethodName(@PathVariable UUID beerId, @RequestBody BeerDTO beer) {
        BeerDTO updatedBeer = this.beerServiceImpl.updatedById(beerId, beer).get();
        HttpHeaders headers = new HttpHeaders();
        headers.add("location", API_V1_BEER + "/" + updatedBeer.getId());
        return new ResponseEntity<BeerDTO>(updatedBeer, headers, HttpStatus.ACCEPTED);
    }

    @DeleteMapping(API_V1_BEER_ID_PATH)
    public ResponseEntity<Object> deleteBearById(@PathVariable("beerId") UUID beerId) {
        this.beerServiceImpl.deletedById(beerId);
        return new ResponseEntity<Object>(HttpStatus.NO_CONTENT);
    }

}
