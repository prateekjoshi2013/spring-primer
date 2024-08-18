package com.prateek.web.springrestdemo.listeners;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.prateek.web.springrestdemo.events.BeerCreatedEvent;

@Component
public class BeerCreateListener {

    @Async
    @EventListener
    public void listen(BeerCreatedEvent event) {
        System.out.println("I heard a beer was created");
        System.out.println(event.getBeer().getId());
        System.out.println("event processing thread id:" + Thread.currentThread().getId());
        System.out.println("event processing thread name:" + Thread.currentThread().getName());
    }
}
