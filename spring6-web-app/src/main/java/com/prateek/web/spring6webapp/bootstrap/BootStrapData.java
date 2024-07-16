package com.prateek.web.spring6webapp.bootstrap;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.prateek.web.spring6webapp.domain.Author;
import com.prateek.web.spring6webapp.domain.Book;
import com.prateek.web.spring6webapp.repositories.AuthorRepository;
import com.prateek.web.spring6webapp.repositories.BookRepository;

@Component
public class BootStrapData implements CommandLineRunner {

    private BookRepository bookRepository;
    private AuthorRepository authorRepository;

    public BootStrapData(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Author author1 = new Author();
        Author author2 = new Author();
        author1.setFirstName("Leo");
        author1.setLastName("Tolstoy");
        author2.setFirstName("Mark");
        author2.setLastName("Twain");
        Book book1 = new Book();
        Book book2 = new Book();
        book1.setIsbn("123123231");
        book1.setTitle("War and Peace");
        book2.setIsbn("452342344");
        book2.setTitle("Adventures of Tom Sawyer");
        Book savedBook1 = bookRepository.save(book1);
        Book savedBook2 = bookRepository.save(book2);
        Author savedAuthor1 = authorRepository.save(author1);
        Author savedAuthor2 = authorRepository.save(author2);
        savedBook1.getAuthors().add(savedAuthor1);
        savedBook2.getAuthors().add(savedAuthor2);

        System.out.println("In Bootstrap");
        System.out.println("Authors:" + authorRepository.count());
        System.out.println("Books:" + bookRepository.count());
    }

}
