package com.prateek.spring.springdi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;

import com.prateek.spring.springdi.domain.MyComponentInterface;

/**
 * - Indicates that an annotated class is a "Controller" (e.g. a web
 * controller).
 *
 * - This annotation serves as a specialization of @Component,
 * - Allowing for implementation classes to be autodetected through classpath
 * scanning.
 * 
 * - It is typically used in combination with annotated handler methods based on
 * the @RequestMapping
 */
@Profile({ "test", "qa" })
@Controller
public class FieldInjectedController {

    @Qualifier("fieldInjectedComponent")
    @Autowired
    private MyComponentInterface myFieldInjectedComponent;

    void run() {
        this.myFieldInjectedComponent.whatAmI();
    }

}
