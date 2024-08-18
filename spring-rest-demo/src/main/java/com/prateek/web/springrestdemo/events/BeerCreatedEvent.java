package com.prateek.web.springrestdemo.events;

import org.springframework.security.core.Authentication;

import com.prateek.web.springrestdemo.domain.entities.Beer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class BeerCreatedEvent {
    private Beer beer;

    private Authentication authentication;
}
