package com.prateek.reactive.webflux.repositories;

import java.time.Duration;

import org.junit.jupiter.api.Test;

import com.prateek.reactive.webflux.domain.Person;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class PersonRepositoryImplTest {

    PersonRepository personRepository = new PersonRepositoryImpl();

    @Test
    void testMonoIdBlock() {
        Mono<Person> personMono = personRepository.getById(1);
        Person person = personMono.block(); // this is blocking operation
        ;
        System.out.println(person);
    }

    @Test
    void testGetByIdSubscriber() {
        Mono<Person> persMono = personRepository.getById(2);
        persMono.subscribe(person -> System.out.println(person));
        // subscriber needed to get the final result
    }

    @Test
    void testMapOperation() {
        Mono<Person> personMono = personRepository.getById(3);
        personMono
                .map(Person::getFirstName)
                .subscribe(firstName -> System.out.println(firstName));
        // subscriber needed to get the final result
    }

    @Test
    void testMonoErrorHandling() {
        personRepository.getById(null)
                .map(Person::getFirstName)
                .onErrorResume(e -> Mono.just(e.getMessage()))
                .subscribe(str -> System.out.println(str));

        Mono<String> mono = Mono.just("Hello, World!")
                .map(value -> {
                    if (value.equals("Hello, World!")) {
                        throw new RuntimeException("Error occurred");
                    }
                    return value;
                });

        mono.subscribe(
                value -> System.out.println(value),
                error -> System.err.println("Error: " + error.getMessage()));
    }

    @Test
    void testCombiningMonos() {
        Mono<Person> byId1 = personRepository.getById(1);
        Mono<Person> byId2 = personRepository.getById(2);
        Mono.zip(byId1, byId2, (person1, person2) -> {
            if (person1.getFirstName().compareTo(person2.getFirstName()) > 0) {
                return Mono.just(person1);
            } else {
                return Mono.just(person2);
            }
        });
    }

    @Test
    void testDelayedResponse() {
        String block = Mono.just("Delayed response")
                .delayElement(Duration.ofSeconds(10))
                .block();
        System.out.println(block);

    }



}
