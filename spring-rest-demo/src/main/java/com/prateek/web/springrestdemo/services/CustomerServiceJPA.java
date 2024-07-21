package com.prateek.web.springrestdemo.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.prateek.web.springrestdemo.domain.exceptions.NoCustomerException;
import com.prateek.web.springrestdemo.mappers.CustomerMapper;
import com.prateek.web.springrestdemo.model.CustomerDTO;
import com.prateek.web.springrestdemo.repositories.CustomerRepository;

import lombok.RequiredArgsConstructor;

@Primary
@Service
@RequiredArgsConstructor
public class CustomerServiceJPA implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    public Optional<CustomerDTO> getCustomerById(UUID customerId) {
        return customerRepository.findById(customerId)
                .map(customer -> Optional.of(customerMapper.customerToCustomerDto(customer)))
                .orElseThrow(() -> new NoCustomerException(customerId.toString()));
    }

    @Override
    public List<CustomerDTO> listCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(customer -> customerMapper.customerToCustomerDto(customer))
                .toList();
    }

    @Override
    public CustomerDTO saveCustomer(CustomerDTO customer) {
        return customerMapper
                .customerToCustomerDto(
                        customerRepository.save(
                                customerMapper.customerDtoToCustomer(customer)));
    }

    @Override
    public Optional<CustomerDTO> updateCustomerById(UUID customerId, CustomerDTO customer) {
        return customerRepository
                .findById(customerId)
                .map(cst -> {
                    cst.setCustomerName(customer.getCustomerName());
                    cst.setLastModifiedDate(LocalDateTime.now());
                    return Optional.of(customerMapper.customerToCustomerDto(customerRepository.save(cst)));
                }).orElseThrow(() -> new NoCustomerException(customerId.toString()));
    }

    @Override
    public void deleteCustomerById(UUID customerId) {
        if (customerRepository.existsById(customerId)) {
            customerRepository.deleteById(customerId);
        } else {
            throw new NoCustomerException(customerId.toString());
        }
    }

}
