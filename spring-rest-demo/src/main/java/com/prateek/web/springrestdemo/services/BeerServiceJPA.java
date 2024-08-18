package com.prateek.web.springrestdemo.services;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.prateek.web.springrestdemo.domain.entities.Beer;
import com.prateek.web.springrestdemo.domain.exceptions.NoBeerFoundException;
import com.prateek.web.springrestdemo.events.BeerCreatedEvent;
import com.prateek.web.springrestdemo.mappers.BeerMapper;
import com.prateek.web.springrestdemo.model.BeerDTO;
import com.prateek.web.springrestdemo.model.BeerStyle;
import com.prateek.web.springrestdemo.repositories.BeerRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Primary
@Service
@RequiredArgsConstructor
public class BeerServiceJPA implements BeerService {
    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;
    private final CacheManager cacheManager;
    private final ApplicationEventPublisher applicationEventPublisher;

    private static final int DEFAULT_PAGE_NUMBER = 0;
    private static final int DEFAULT_PAGE_SIZE = 25;
    private static final int MAX_PAGE_SIZE = 1000;

    public PageRequest buildPageRequest(Integer pageNumber, Integer pageSize) {
        int queryPageNumer = Optional.ofNullable(pageNumber).map(pgn -> pgn - 1).orElse(DEFAULT_PAGE_NUMBER);
        int queryPageSize = Optional.ofNullable(pageSize).map(pgs -> pgs > MAX_PAGE_SIZE ? MAX_PAGE_SIZE : pgs)
                .orElse(DEFAULT_PAGE_SIZE);
        Sort sort = Sort.by(Sort.Order.asc("beerName"));
        return PageRequest.of(queryPageNumer, queryPageSize, sort);
    }

    @Override
    public void deletedById(UUID beerId) {
        cacheManager.getCache("beerCache").evict(beerId);
        cacheManager.getCache("beerListCache").clear();
        if (beerRepository.existsById(beerId)) {
            beerRepository.deleteById(beerId);
        } else {
            throw new NoBeerFoundException(beerId.toString());
        }
    }

    @Cacheable(cacheNames = "beerCache", key = "#id")
    @Override
    public Optional<BeerDTO> getBeerById(UUID id) {
        log.info("Get Beer by Id - in service");
        return beerRepository.findById(id).map(beer -> {
            return Optional.of(beerMapper.beerToBeerDto(beer));
        }).orElseThrow(() -> new NoBeerFoundException(id.toString()));
    }

    @Cacheable(cacheNames = "beerListCache")
    // creates a default key by combining all params
    @Override
    public Page<BeerDTO> listBeers(String beerName, BeerStyle beerStyle, Boolean showInvetory, Integer pageNumber,
            Integer pageSize) {
        log.info("Get Beer List - in service");
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize);
        Page<Beer> beerPage;
        if (StringUtils.hasText(beerName) && beerStyle != null) {
            beerPage = beerRepository
                    .findAllByBeerNameIsLikeIgnoreCaseAndBeerStyle("%" + beerName + "%", beerStyle, pageRequest);
        } else if (StringUtils.hasText(beerName) && beerStyle == null) {
            beerPage = beerRepository
                    .findAllByBeerNameIsLikeIgnoreCase("%" + beerName + "%", pageRequest);
        } else if (!StringUtils.hasText(beerName) && beerStyle != null) {
            beerPage = beerRepository.findAllByBeerStyle(beerStyle, pageRequest);
        } else {
            beerPage = beerRepository.findAll(pageRequest);
        }
        return beerPage
                .map(beerMapper::beerToBeerDto)
                .map(beer -> {
                    if (showInvetory == null || !showInvetory) {
                        beer.setQuantityOnHand(null);
                        return beer;
                    } else {
                        return beer;
                    }
                });
    }

    @Override
    public BeerDTO saveNewBeer(BeerDTO beer) {
        cacheManager.getCache("beerListCache").clear();
        Beer savedBeer = beerRepository.save(beerMapper.beerDtoToBeer(beer));
        System.out.println("Saving thread id:" + Thread.currentThread().getId());
        System.out.println("Saving thread name:" + Thread.currentThread().getName());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        applicationEventPublisher
                .publishEvent(BeerCreatedEvent.builder().beer(savedBeer).authentication(authentication).build());
        return beerMapper.beerToBeerDto(savedBeer);
    }

    @Override
    public Optional<BeerDTO> updatedById(UUID beerId, BeerDTO beer) {
        cacheManager.getCache("beerCache").evict(beerId);
        cacheManager.getCache("beerListCache").clear();
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
