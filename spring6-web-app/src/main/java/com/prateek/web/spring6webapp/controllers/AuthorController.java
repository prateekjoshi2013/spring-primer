package com.prateek.web.spring6webapp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.prateek.web.spring6webapp.repositories.AuthorRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthorController {

    private AuthorRepository authorRepository;

    public AuthorController(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @GetMapping("/authors")
    String getAllAuthors(Model model) {
        model.addAttribute("authors", this.authorRepository.findAll());
        return "authors";
    }
}
