package com.prateek.spring.springdi.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("staging")
@SpringBootTest
public class SetterInjectedControllerTest {

    @Autowired
    private SetterInjectedController setterInjectedController;

    @Test
    void testRun() {
        setterInjectedController.run();
    }
}
