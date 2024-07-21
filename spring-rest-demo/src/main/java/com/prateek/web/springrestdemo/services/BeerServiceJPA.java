package com.prateek.web.springrestdemo.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.prateek.web.springrestdemo.domain.exceptions.NoBeerFoundException;
import com.prateek.web.springrestdemo.mappers.BeerMapper;
import com.prateek.web.springrestdemo.model.BeerDTO;
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
        // TODO Auto-generated method stub

    }

    @Override
    public Optional<BeerDTO> getBeerById(UUID id) {
        return beerRepository.findById(id).map(beer -> {
            return Optional.of(beerMapper.beerToBeerDto(beer));
        }).orElseThrow(() -> new NoBeerFoundException(id.toString()));
    }

    @Override
    public List<BeerDTO> listBeers() {
        return beerRepository
                .findAll()
                .stream()
                .map(beer -> beerMapper.beerToBeerDto(beer)).toList();
    }

    @Override
    public BeerDTO saveNewBeer(BeerDTO beer) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Optional<BeerDTO> updatedById(UUID beerId, BeerDTO beer) {
        // TODO Auto-generated method stub
        return Optional.empty();
    }

}
