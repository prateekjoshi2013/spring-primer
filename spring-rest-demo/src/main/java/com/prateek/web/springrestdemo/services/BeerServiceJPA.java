package com.prateek.web.springrestdemo.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.prateek.web.springrestdemo.domain.entities.Beer;
import com.prateek.web.springrestdemo.domain.exceptions.NoBeerFoundException;
import com.prateek.web.springrestdemo.mappers.BeerMapper;
import com.prateek.web.springrestdemo.model.BeerDTO;
import com.prateek.web.springrestdemo.model.BeerStyle;
import com.prateek.web.springrestdemo.repositories.BeerRepository;

import lombok.RequiredArgsConstructor;

@Primary
@Service
@RequiredArgsConstructor
public class BeerServiceJPA implements BeerService {
    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;

    @Override
    public void deletedById(UUID beerId) {
        if (beerRepository.existsById(beerId)) {
            beerRepository.deleteById(beerId);
        } else {
            throw new NoBeerFoundException(beerId.toString());
        }
    }

    @Override
    public Optional<BeerDTO> getBeerById(UUID id) {
        return beerRepository.findById(id).map(beer -> {
            return Optional.of(beerMapper.beerToBeerDto(beer));
        }).orElseThrow(() -> new NoBeerFoundException(id.toString()));
    }

    @Override
    public List<BeerDTO> listBeers(String beerName, BeerStyle beerStyle, Boolean showInvetory) {
        List<Beer> results = null;
        if (StringUtils.hasText(beerName) && beerStyle != null) {
            results = beerRepository
                    .findAllByBeerNameIsLikeIgnoreCaseAndBeerStyle("%" + beerName + "%", beerStyle);
        } else if (StringUtils.hasText(beerName) && beerStyle == null) {
            results = beerRepository
                    .findAllByBeerNameIsLikeIgnoreCase("%" + beerName + "%");
        } else if (!StringUtils.hasText(beerName) && beerStyle != null) {
            results = beerRepository.findAllByBeerStyle(beerStyle);
        } else {
            results = beerRepository.findAll().stream().toList();
        }

        return results.stream()
                .map(beerMapper::beerToBeerDto)
                .map(beer -> {
                    if (showInvetory == null || !showInvetory) {
                        beer.setQuantityOnHand(null);
                        return beer;
                    } else {
                        return beer;
                    }
                }).toList();
    }

    @Override
    public BeerDTO saveNewBeer(BeerDTO beer) {
        return beerMapper.beerToBeerDto(beerRepository.save(beerMapper.beerDtoToBeer(beer)));
    }

    @Override
    public Optional<BeerDTO> updatedById(UUID beerId, BeerDTO beer) {
        return beerRepository
                .findById(beerId)
                .map(oldBeer -> {
                    oldBeer.setBeerName(beer.getBeerName());
                    oldBeer.setBeerStyle(beer.getBeerStyle());
                    oldBeer.setUpdateDate(LocalDateTime.now());
                    oldBeer.setPrice(beer.getPrice());
                    oldBeer.setUpc(beer.getUpc());
                    oldBeer.setQuantityOnHand(beer.getQuantityOnHand());
                    Beer updateBeer = beerRepository.save(oldBeer);
                    return Optional.of(beerMapper.beerToBeerDto(updateBeer));
                }).orElseThrow(() -> new NoBeerFoundException(beerId.toString()));
    }

}
