package com.prateek.web.springrestdemo.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.prateek.web.springrestdemo.domain.entities.Beer;
import com.prateek.web.springrestdemo.domain.exceptions.NoBeerFoundException;
import com.prateek.web.springrestdemo.model.BeerDTO;
import com.prateek.web.springrestdemo.model.BeerStyle;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BeerServiceImpl implements BeerService {
        private Map<UUID, BeerDTO> beerMap = Stream.of(
                        BeerDTO.builder()
                                        .id(UUID.randomUUID())
                                        .beerName("indian pale ale")
                                        .price(BigDecimal.valueOf(12.023))
                                        .beerStyle(BeerStyle.PALE_ALE)
                                        .quantityOnHand(12)
                                        .upc("upc")
                                        .version(2)
                                        .createdDate(LocalDateTime.now())
                                        .updateDate(LocalDateTime.now())
                                        .build(),
                        BeerDTO.builder()
                                        .id(UUID.randomUUID())
                                        .beerName("Fosters")
                                        .price(BigDecimal.valueOf(10.023))
                                        .beerStyle(BeerStyle.PILSNER)
                                        .quantityOnHand(10)
                                        .upc("upc")
                                        .version(2)
                                        .createdDate(LocalDateTime.now())
                                        .updateDate(LocalDateTime.now())
                                        .build(),
                        BeerDTO.builder()
                                        .id(UUID.randomUUID())
                                        .beerName("Kingfisher")
                                        .price(BigDecimal.valueOf(10.023))
                                        .beerStyle(BeerStyle.STOUT)
                                        .quantityOnHand(10)
                                        .upc("upc")
                                        .version(2)
                                        .createdDate(LocalDateTime.now())
                                        .updateDate(LocalDateTime.now())
                                        .build())
                        .collect(Collectors.toMap(beer -> beer.getId(), beer -> beer));

        public Page<BeerDTO> listBeers(String beerName, BeerStyle beerStyle, Boolean showInvetory, Integer pageNumber,
                        Integer pageSize) {
                log.info("sending the list of beers");
                List<BeerDTO> results = null;
                if (StringUtils.hasText(beerName) && beerStyle != null) {
                        results = beerMap.values().stream().filter(beerDto -> beerDto.getBeerName().contains(beerName)
                                        && beerDto.getBeerStyle() == beerStyle).toList();
                } else if (StringUtils.hasText(beerName) && beerStyle == null) {
                        results = beerMap.values().stream().filter(beerDto -> beerDto.getBeerName().contains(beerName))
                                        .toList();
                } else if (!StringUtils.hasText(beerName) && beerStyle != null) {
                        results = beerMap.values().stream().filter(beerDto -> beerDto.getBeerStyle() == beerStyle)
                                        .toList();
                } else {
                        results = beerMap.values().stream().toList();
                }

                return new PageImpl<>(results.stream()
                                .map(beer -> {
                                        if (showInvetory == null || !showInvetory) {
                                                beer.setQuantityOnHand(null);
                                                return beer;
                                        } else {
                                                return beer;
                                        }
                                }).toList());

        }

        @Override
        public Optional<BeerDTO> getBeerById(UUID id) {
                return Optional.ofNullable(this.beerMap.get(id))
                                .map(beer -> {
                                        log.info("sending the beer object with id {}: {}", beer.getId(), beer);
                                        return Optional.of(beer);
                                }).orElseThrow(() -> new NoBeerFoundException(id.toString()));
        }

        @Override
        public BeerDTO saveNewBeer(BeerDTO beer) {
                BeerDTO savedBeer = BeerDTO.builder()
                                .id(UUID.randomUUID())
                                .beerName(beer.getBeerName())
                                .price(beer.getPrice())
                                .beerStyle(beer.getBeerStyle())
                                .quantityOnHand(beer.getQuantityOnHand())
                                .upc(beer.getUpc())
                                .version(beer.getVersion())
                                .createdDate(LocalDateTime.now())
                                .updateDate(LocalDateTime.now())
                                .build();

                this.beerMap.put(savedBeer.getId(), savedBeer);
                return savedBeer;
        }

        @Override
        public Optional<BeerDTO> updatedById(UUID beerId, BeerDTO beer) {
                return Optional.ofNullable(this.beerMap.get(beerId)).map(
                                updatedBeer -> {

                                        updatedBeer.setBeerName(beer.getBeerName());
                                        updatedBeer.setBeerStyle(beer.getBeerStyle());
                                        updatedBeer.setPrice(beer.getPrice());
                                        updatedBeer.setQuantityOnHand(beer.getQuantityOnHand());
                                        updatedBeer.setUpc(beer.getUpc());
                                        updatedBeer.setUpdateDate(LocalDateTime.now());
                                        log.info("Updated beer : {}", updatedBeer);
                                        return Optional.of(updatedBeer);
                                }).orElseThrow(() -> new NoBeerFoundException(beerId.toString()));
        }

        @Override
        public void deletedById(UUID beerId) {
                Optional.ofNullable(this.beerMap.get(beerId)).ifPresent(beer -> {
                        log.info("deleteing object :{}", this.beerMap.get(beerId));
                        beerMap.remove(beerId);
                });
        }

}
