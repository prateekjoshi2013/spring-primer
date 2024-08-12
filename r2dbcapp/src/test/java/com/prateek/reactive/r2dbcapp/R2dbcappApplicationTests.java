package com.prateek.reactive.r2dbcapp;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import com.prateek.reactive.r2dbcapp.repositories.BeerRepository;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class R2dbcappApplicationTests {
	@Autowired
	BeerRepository beerRepository;

	@Test
	void contextLoads() {
	}

	@AfterAll
	void teardown() {
		System.out.println("Running after all tests");
		// Cleanup logic
		beerRepository
				.deleteAll().block();
	}
}
