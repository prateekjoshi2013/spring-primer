package com.prateek.web.springrestdemo.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prateek.web.springrestdemo.domain.entities.Customer;

public interface CustomerRepository extends JpaRepository<Customer,UUID> {

}
