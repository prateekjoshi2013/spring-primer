package com.prateek.web.spring6webapp.services;

import org.springframework.stereotype.Service;

import com.prateek.web.spring6webapp.domain.Book;
import com.prateek.web.spring6webapp.repositories.BookRepository;

@Service
public class BookServiceImpl implements BookService {

    private BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Iterable<Book> findAll() {
        return this.bookRepository.findAll();
    }

}
