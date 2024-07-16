package com.prateek.web.spring6webapp.repositories;

import org.springframework.data.repository.CrudRepository;

import com.prateek.web.spring6webapp.domain.Author;

public interface AuthorRepository extends CrudRepository<Author, Long> {

}
