package com.prateek.web.spring6webapp.services;

import org.springframework.stereotype.Service;

import com.prateek.web.spring6webapp.domain.Author;
import com.prateek.web.spring6webapp.repositories.AuthorRepository;

@Service
public class AuthorServiceImpl implements AuthorService {

    private AuthorRepository authorRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public Iterable<Author> findAll() {
        return this.authorRepository.findAll();
    }

}
