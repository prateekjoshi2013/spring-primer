package com.prateek.spring.springdi.controllers;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

import com.prateek.spring.springdi.domain.MyComponentInterface;

@Controller
public class ConstructorInjectedController {
    // declare private final as it is initialized from constructor
    private final MyComponentInterface myComponentInterface;

    // @Autowired no need to annotate gets Autowried by default with Qualified bean
    // no need to provide name to bean with @Component("constructorInjected") if the
    // qualifier value matches the lowercased Bean name
    // for ex here: class name is ContructorInjected
    public ConstructorInjectedController(@Qualifier("constructorInjected") MyComponentInterface myComponentInterface) {
        this.myComponentInterface = myComponentInterface;
    }

    void run() {
        this.myComponentInterface.whatAmI();
    }

}
