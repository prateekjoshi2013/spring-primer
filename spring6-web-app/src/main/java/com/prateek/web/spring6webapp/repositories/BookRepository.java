package com.prateek.web.spring6webapp.repositories;

import org.springframework.data.repository.CrudRepository;

import com.prateek.web.spring6webapp.domain.Book;

public interface BookRepository extends CrudRepository<Book, Long> {

}
