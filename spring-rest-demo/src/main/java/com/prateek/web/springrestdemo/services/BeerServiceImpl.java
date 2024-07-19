package com.prateek.web.springrestdemo.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.prateek.web.springrestdemo.model.Beer;
import com.prateek.web.springrestdemo.model.BeerStyle;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BeerServiceImpl implements BeerService {

    @Override
    public Beer getBeerById(UUID id) {

        Beer beer = Beer.builder()
                .id(id)
                .beerName("indian pale ale")
                .price(BigDecimal.valueOf(12.023))
                .beerStyle(BeerStyle.PALE_ALE)
                .quantityOnHand(12)
                .upc("upc")
                .version(1)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();
        log.info("sending the beer object with id {}: {}", id, beer);
        return beer;
    }

}
