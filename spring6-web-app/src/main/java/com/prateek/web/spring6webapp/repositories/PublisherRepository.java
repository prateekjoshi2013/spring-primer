package com.prateek.web.spring6webapp.repositories;

import org.springframework.data.repository.CrudRepository;

import com.prateek.web.spring6webapp.domain.Publisher;

public interface PublisherRepository extends CrudRepository<Publisher, Long> {

}
