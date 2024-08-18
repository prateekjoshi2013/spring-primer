package com.prateek.web.springrestdemo.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prateek.web.springrestdemo.domain.entities.BeerAudit;

public interface BeerAuditRepository extends JpaRepository<BeerAudit, UUID> {

}
