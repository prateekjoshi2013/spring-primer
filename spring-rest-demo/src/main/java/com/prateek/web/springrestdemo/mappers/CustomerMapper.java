package com.prateek.web.springrestdemo.mappers;

import org.mapstruct.Mapper;

import com.prateek.web.springrestdemo.domain.entities.Customer;
import com.prateek.web.springrestdemo.model.CustomerDTO;

@Mapper
public interface CustomerMapper {
    Customer customerDtoToCustomer(CustomerDTO dto);

    CustomerDTO customerToCustomerDto(Customer customer);
}
