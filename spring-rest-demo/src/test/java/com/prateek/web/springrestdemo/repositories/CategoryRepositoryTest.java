package com.prateek.web.springrestdemo.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.EnabledIf;

import com.prateek.web.springrestdemo.domain.entities.Beer;
import com.prateek.web.springrestdemo.domain.entities.Category;

import jakarta.transaction.Transactional;

@EnabledIf(value = "#{{'default','localmysql'}.contains(environment.getActiveProfiles()[0])}", loadContext = true)
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
        assertEquals(saveBeer.getCategories().size(), 1);
    }
}
