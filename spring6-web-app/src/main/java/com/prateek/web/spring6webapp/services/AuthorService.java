package com.prateek.web.spring6webapp.services;

import com.prateek.web.spring6webapp.domain.Author;

public interface AuthorService {

    Iterable<Author> findAll();

}
