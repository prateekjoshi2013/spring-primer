package com.prateek.web.springrestdemo.services;

import java.util.UUID;

import com.prateek.web.springrestdemo.model.Beer;

public interface BeerService {
    Beer getBeerById(UUID id);
}
