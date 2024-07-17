package com.prateek.spring.springdi.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ConstructorInjectedControllerTest {

    @Qualifier("constructorInjectedController")
    @Autowired
    private ConstructorInjectedController constructorInjectedController;

    @Test
    void testRun() {
        constructorInjectedController.run();
    }
}
