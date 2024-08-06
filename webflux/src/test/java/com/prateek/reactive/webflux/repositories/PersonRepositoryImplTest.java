package com.prateek.reactive.webflux.repositories;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    @Test
    void testFluxBlockFirst() {
        Flux<Person> personFlux = personRepository.findAll();
        Person blockFirst = personFlux.blockFirst();
        System.out.println(blockFirst);
    }

    @Test
    void testFluxSubscriber() {
        personRepository.findAll().subscribe(person -> System.out.println(person));
    }

    @Test
    void testFluxMap() {
        personRepository.findAll().map(Person::getFirstName).subscribe(name -> System.out.println(name));
    }

    @Test
    void filterFlux() {
        personRepository.findAll()
                .filter(person -> person.getId() > 3)
                .subscribe(person -> System.out.println(person));
    }

    @Test
    void combineMultipleFluxes() {
        Flux<Person> personFlux1 = personRepository.findAll();
        Mono<List<Person>> collectList = personRepository.findAll().collectList();
        /**
         * flatMapMany is a method in the Reactor library used to transform a Mono into a Flux. 
         * This is particularly useful when you have a Mono that emits a single value which is 
         * a collection or a stream, and you want to flatten this collection or stream into 
         * individual elements that can be processed reactively.
         */
        Flux<Person> personFlux2 = collectList.flatMapMany(list -> {
            Collections.reverse(list);
            return Flux.fromIterable(list);
        });
        Flux.zip(personFlux1, personFlux2.take(2), (person1, person2) -> {
            System.out.println("Person1:" + person1);
            System.out.println("Person2:" + person2);
            if (person1.getFirstName().compareTo(person2.getFirstName()) > 0) {
                return person1;
            } else {
                return person2;
            }
        }).subscribe(person -> System.out.println(person));
    }

    @Test
    void processRateLimt() {
        List<String> largeData = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            largeData.add("Data " + i);
        }

        Flux.fromIterable(largeData)
                .limitRate(100);
        // Handle backpressure by limiting the rate
    }

    @Test
    void mergingFluxItems() {
        Flux<Person> flux1 = personRepository.findAll().takeLast(2);
        Flux<Person> flux2 = personRepository.findAll().take(2);
        Flux.concat(flux1, flux2).subscribe(System.out::println);
    }

}
