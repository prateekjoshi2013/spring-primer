package com.prateek.web.springrestdemo.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prateek.web.springrestdemo.domain.entities.Beer;

public interface BeerRepository extends JpaRepository<Beer, UUID> {

}