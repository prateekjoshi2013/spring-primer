package com.prateek.spring.springdi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

import com.prateek.spring.springdi.domain.MyComponentInterface;

@Controller
public class SetterInjectedController {

    private MyComponentInterface mySetterInjected;

    @Qualifier("setterInjected")
    @Autowired
    public void setMySetterInjected(MyComponentInterface mySetterInjected) {
        System.out.println("injecting field");
        this.mySetterInjected = mySetterInjected;

    }

    void run() {
        this.mySetterInjected.whatAmI();
    }

}
