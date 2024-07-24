package com.prateek.web.springrestdemo.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.prateek.web.springrestdemo.domain.entities.Beer;
import com.prateek.web.springrestdemo.domain.entities.Category;
import com.prateek.web.springrestdemo.domain.entities.Customer;

import jakarta.transaction.Transactional;

@SpringBootTest
public class CategoryRepositoryTest {
    @Autowired
    BeerRepository beerRepository;

    @Autowired
    CategoryRepository categoryRepository;

    Beer testBeer;

    @BeforeEach
    void setUp() {
        this.testBeer = beerRepository.findAll().get(0);
    }

    @Test
    @Transactional
    void testAddCategory() {
        Category savedCat = categoryRepository.save(
                Category
                        .builder()
                        .description("Ales")
                        .build());
        testBeer.addCategory(savedCat);
        Beer saveBeer = beerRepository.save(testBeer);
        System.out.println(saveBeer.getBeerName());
    }
}
