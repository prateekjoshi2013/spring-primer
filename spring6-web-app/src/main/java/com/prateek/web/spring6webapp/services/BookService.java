package com.prateek.web.spring6webapp.services;

import com.prateek.web.spring6webapp.domain.Book;

public interface BookService {
    Iterable<Book> findAll();
}
