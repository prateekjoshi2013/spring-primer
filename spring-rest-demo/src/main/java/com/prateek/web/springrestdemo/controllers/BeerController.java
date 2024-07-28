package com.prateek.web.springrestdemo.controllers;

import java.util.List;
import java.util.UUID;

import com.prateek.web.springrestdemo.model.BeerDTO;
import com.prateek.web.springrestdemo.model.BeerStyle;
import com.prateek.web.springrestdemo.services.BeerService;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RequiredArgsConstructor
@RestController
public class BeerController {

    public static final String API_V1_BEER = "/api/v1/beer";
    public static final String CUSTOMER_ID = "/{beerId}";
    public static final String API_V1_BEER_ID_PATH = API_V1_BEER + CUSTOMER_ID;
    private final BeerService beerServiceImpl;

    @GetMapping(API_V1_BEER)
    public Page<BeerDTO> listBeers(@RequestParam(name = "beerName", required = false) String beerName,
            @RequestParam(name = "beerStyle", required = false) BeerStyle beerStyle,
            @RequestParam(name = "showInventory", required = false) Boolean showInventory,
            @RequestParam(name = "pageSize", required = false) Integer pageSize,
            @RequestParam(name = "pageNumber", required = false) Integer pageNumber) {
        return beerServiceImpl.listBeers(beerName, beerStyle, showInventory, pageNumber, pageSize);
    }

    @GetMapping(API_V1_BEER_ID_PATH)
    public BeerDTO getBeerById(@PathVariable("beerId") UUID beerId) {
        return beerServiceImpl.getBeerById(beerId).get();
    }

    @PostMapping(API_V1_BEER)
    public ResponseEntity<BeerDTO> postBeer(@Validated @RequestBody BeerDTO beer) {
        BeerDTO savedBeer = this.beerServiceImpl.saveNewBeer(beer);
        HttpHeaders headers = new HttpHeaders();
        headers.add("location", API_V1_BEER + "/" + savedBeer.getId());
        return new ResponseEntity<BeerDTO>(savedBeer, headers, HttpStatus.CREATED);
    }

    @PutMapping(API_V1_BEER_ID_PATH)
    public ResponseEntity<BeerDTO> updateBeer(@NotNull @PathVariable UUID beerId,
            @Validated @RequestBody BeerDTO beer) {
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
