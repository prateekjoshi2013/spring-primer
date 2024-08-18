package com.prateek.web.springrestdemo.listeners;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.prateek.web.springrestdemo.domain.entities.BeerAudit;
import com.prateek.web.springrestdemo.events.BeerCreatedEvent;
import com.prateek.web.springrestdemo.mappers.BeerMapper;
import com.prateek.web.springrestdemo.repositories.BeerAuditRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class BeerCreateListener {

    private final BeerMapper beerMapper;
    private final BeerAuditRepository beerAuditRepository;

    @Async
    @EventListener
    public void listen(BeerCreatedEvent event) {
        BeerAudit beerToBeerAudit = beerMapper.beerToBeerAudit(event.getBeer());
        if (event.getAuthentication() != null && event.getAuthentication().getName() != null) {
            beerToBeerAudit.setPrincipalName(event.getAuthentication().getName());
        }
        beerToBeerAudit.setAuditEventType("BEER_CREATED");
        beerAuditRepository.save(beerToBeerAudit);
        log.debug("event processing thread id:" + Thread.currentThread().getId());
        log.debug("event processing thread name:" + Thread.currentThread().getName());
        log.debug("Audit created");
    }
}
