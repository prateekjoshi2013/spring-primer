package com.prateek.web.spring6webapp.bootstrap;

import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.prateek.web.spring6webapp.domain.Author;
import com.prateek.web.spring6webapp.domain.Book;
import com.prateek.web.spring6webapp.domain.Publisher;
import com.prateek.web.spring6webapp.repositories.AuthorRepository;
import com.prateek.web.spring6webapp.repositories.BookRepository;
import com.prateek.web.spring6webapp.repositories.PublisherRepository;

@Component
public class BootStrapData implements CommandLineRunner {

    private BookRepository bookRepository;
    private AuthorRepository authorRepository;
    private PublisherRepository publisherRepository;

    public BootStrapData(BookRepository bookRepository, AuthorRepository authorRepository,
            PublisherRepository publisherRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.publisherRepository = publisherRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // create authors, books and publisher
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
        Publisher publisher = new Publisher();
        publisher.setAddress("1212 Beverly Hills");
        publisher.setZip("85286");
        publisher.setCity("LA");
        publisher.setState("CA");
        publisher.setPublisherName("Pearson");
        // save books, authors & publishers
        Book savedBook1 = bookRepository.save(book1);
        Book savedBook2 = bookRepository.save(book2);
        Author savedAuthor1 = authorRepository.save(author1);
        Author savedAuthor2 = authorRepository.save(author2);
        Publisher savedPublisher = publisherRepository.save(publisher);
        // update relationships
        savedBook1.getAuthors().add(savedAuthor1);
        savedBook2.getAuthors().add(savedAuthor2);
        savedAuthor1.getBooks().add(savedBook1);
        savedAuthor2.getBooks().add(savedBook2);
        savedPublisher.getBooks().addAll(Arrays.asList(book1, book2));
        savedBook2.setPublisher(savedPublisher);
        savedBook1.setPublisher(savedPublisher);
        // save objects with relationships
        savedBook1 = bookRepository.save(book1);
        savedBook2 = bookRepository.save(book2);
        savedAuthor1 = authorRepository.save(author1);
        savedAuthor2 = authorRepository.save(author2);
        savedPublisher = publisherRepository.save(publisher);

        System.out.println("In Bootstrap");
        System.out.println("Authors:" + authorRepository.count());
        System.out.println("Books:" + bookRepository.count());
        System.out.println("Publishers:" + publisherRepository.count());
    }

}
