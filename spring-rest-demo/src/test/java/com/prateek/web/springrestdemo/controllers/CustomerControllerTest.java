package com.prateek.web.springrestdemo.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prateek.web.springrestdemo.domain.exceptions.NoCustomerException;
import com.prateek.web.springrestdemo.model.CustomerDTO;
import com.prateek.web.springrestdemo.services.CustomerService;

import lombok.SneakyThrows;

@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {

    @MockBean
    private CustomerService customerService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    private List<CustomerDTO> mockedCustomers = Stream.of(
            CustomerDTO.builder()
                    .version(1)
                    .id(UUID.randomUUID())
                    .customerName("John")
                    .createdDate(LocalDateTime.now())
                    .lastModifiedDate(LocalDateTime.now())
                    .build(),
            CustomerDTO.builder()
                    .version(2)
                    .id(UUID.randomUUID())
                    .customerName("Jane")
                    .createdDate(LocalDateTime.now())
                    .lastModifiedDate(LocalDateTime.now())
                    .build(),
            CustomerDTO.builder()
                    .version(3)
                    .id(UUID.randomUUID())
                    .customerName("Kane")
                    .createdDate(LocalDateTime.now())
                    .lastModifiedDate(LocalDateTime.now())
                    .build())
            .collect(Collectors.toList());

    @SneakyThrows
    @Test
    void testListCustomers() {
        given(customerService.listCustomers()).willReturn(mockedCustomers);

        mockMvc.perform(get(CustomerController.API_V1_CUSTOMER)
                .accept(MediaType.APPLICATION_JSON))
                // Assert
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // jsonpath documentation : https://github.com/json-path/JsonPath
                .andExpect(jsonPath("$.length()", is(3)));
    }

    @SneakyThrows
    @Test
    void testGetCustomerById() {
        // Get Mocked Beer
        CustomerDTO customer = mockedCustomers.get(0);
        // Set up behaviours and mocks
        given(customerService.getCustomerById(any(UUID.class))).willReturn(Optional.of(customer));

        // Act
        mockMvc.perform(get(CustomerController.API_V1_CUSTOMER_PATH_ID, customer.getId())
                .accept(MediaType.APPLICATION_JSON))
                // Assert
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // jsonpath documentation : https://github.com/json-path/JsonPath
                .andExpect(jsonPath("$.id", is(customer.getId().toString())))
                .andExpect(jsonPath("$.customerName", is(customer.getCustomerName())));

    }

    @SneakyThrows
    @Test
    void testCreateBeer() {

        given(customerService.saveCustomer(any(CustomerDTO.class))).willReturn(mockedCustomers.get(1));

        mockMvc.perform(
                post(CustomerController.API_V1_CUSTOMER)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockedCustomers.get(1)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().exists("location"));

    }

    @SneakyThrows
    @Test
    void testUpdateBeer() {
        // set up mock
        CustomerDTO customer = mockedCustomers.get(0);

        // set up behaviour
        given(customerService.updateCustomerById(any(UUID.class), any(CustomerDTO.class)))
                .willReturn(Optional.of(customer));

        // act
        mockMvc.perform(
                put(CustomerController.API_V1_CUSTOMER_PATH_ID, customer.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                // accept
                .andExpect(status().isAccepted());

        // verify
        verify(customerService).updateCustomerById(any(UUID.class), any(CustomerDTO.class));
    }

    @SneakyThrows
    @Test
    void testDeleteBeer() {
        CustomerDTO customer = mockedCustomers.get(0);

        mockMvc.perform(
                delete(CustomerController.API_V1_CUSTOMER_PATH_ID, customer.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        ArgumentCaptor<UUID> argumentCaptorUUID = ArgumentCaptor.forClass(UUID.class);
        verify(customerService).deleteCustomerById(argumentCaptorUUID.capture());
        assertThat(customer.getId()).isEqualTo(argumentCaptorUUID.getValue());
    }

    @Test
    void testException() throws Exception {
        // Set up behaviours and mocks
        UUID customerId = UUID.randomUUID();
        when(customerService.getCustomerById(any(UUID.class)))
                .thenThrow(new NoCustomerException(customerId.toString()));

        // Act
        mockMvc.perform(get(CustomerController.API_V1_CUSTOMER_PATH_ID, customerId.toString()))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // jsonpath documentation : https://github.com/json-path/JsonPath
                .andExpect(jsonPath("$.message", is("Customer with id: " + customerId + " not found")))
                .andExpect(jsonPath("$.details", is("uri=/api/v1/customer/" + customerId)));
    }
}