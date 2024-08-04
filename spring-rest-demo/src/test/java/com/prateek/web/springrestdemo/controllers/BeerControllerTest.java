package com.prateek.web.springrestdemo.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.context.junit.jupiter.EnabledIf;

import java.math.BigDecimal;
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
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prateek.web.springrestdemo.config.SpringSecConfig;
import com.prateek.web.springrestdemo.domain.exceptions.NoBeerFoundException;
import com.prateek.web.springrestdemo.domain.exceptions.ValidationException;
import com.prateek.web.springrestdemo.model.BeerDTO;
import com.prateek.web.springrestdemo.model.BeerStyle;
import com.prateek.web.springrestdemo.services.BeerService;

import lombok.SneakyThrows;

@EnabledIf(value = "#{{'default'}.contains(environment.getActiveProfiles()[0])}", loadContext = false)
@WebMvcTest(BeerController.class)
@Import(SpringSecConfig.class)
public class BeerControllerTest {

        @Autowired
        MockMvc mockMvc;

        @Autowired
        ObjectMapper objectMapper;

        @MockBean
        BeerService beerService;

        List<BeerDTO> mockedBeers = Stream.of(
                        BeerDTO.builder()
                                        .id(UUID.randomUUID())
                                        .beerName("indian pale ale")
                                        .price(BigDecimal.valueOf(12.023))
                                        .beerStyle(BeerStyle.PALE_ALE)
                                        .quantityOnHand(12)
                                        .upc("upc")
                                        .version(2)
                                        .createdDate(LocalDateTime.now())
                                        .updateDate(LocalDateTime.now())
                                        .build(),
                        BeerDTO.builder()
                                        .id(UUID.randomUUID())
                                        .beerName("Fosters")
                                        .price(BigDecimal.valueOf(10.023))
                                        .beerStyle(BeerStyle.PILSNER)
                                        .quantityOnHand(10)
                                        .upc("upc")
                                        .version(2)
                                        .createdDate(LocalDateTime.now())
                                        .updateDate(LocalDateTime.now())
                                        .build(),
                        BeerDTO.builder()
                                        .id(UUID.randomUUID())
                                        .beerName("Kingfisher")
                                        .price(BigDecimal.valueOf(10.023))
                                        .beerStyle(BeerStyle.STOUT)
                                        .quantityOnHand(10)
                                        .upc("upc")
                                        .version(2)
                                        .createdDate(LocalDateTime.now())
                                        .updateDate(LocalDateTime.now())
                                        .build())
                        .collect(Collectors.toList());

        @Test
        @SneakyThrows
        void testCreateNewBearJsonString() {
                System.out.println(objectMapper.writeValueAsString(mockedBeers.get(0)));
        }

        @SneakyThrows
        @Test
        void testDeleteBeer() {
                BeerDTO beer = mockedBeers.get(0);

                mockMvc.perform(
                                delete(BeerController.API_V1_BEER_ID_PATH, beer.getId())
                                .with(httpBasic("user", "password"))
                                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNoContent());

                ArgumentCaptor<UUID> argumentCaptorUUID = ArgumentCaptor.forClass(UUID.class);
                verify(beerService).deletedById(argumentCaptorUUID.capture());
                assertThat(beer.getId()).isEqualTo(argumentCaptorUUID.getValue());
        }

        @SneakyThrows
        @Test
        void testUpdateBeer() {
                // set up mock
                BeerDTO beer = mockedBeers.get(0);

                // set up behaviour
                given(beerService.updatedById(any(UUID.class), any(BeerDTO.class))).willReturn(Optional.of(beer));

                // act
                mockMvc.perform(
                                put(BeerController.API_V1_BEER_ID_PATH, beer.getId())
                                .with(httpBasic("user", "password"))
                                                .accept(MediaType.APPLICATION_JSON)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(objectMapper.writeValueAsString(beer)))
                                // accept
                                .andExpect(status().isAccepted());

                // verify
                verify(beerService).updatedById(any(UUID.class), any(BeerDTO.class));
        }

        @SneakyThrows
        @Test
        void testCreateBeer() {

                given(beerService.saveNewBeer(any(BeerDTO.class))).willReturn(mockedBeers.get(0));

                mockMvc.perform(
                                post(BeerController.API_V1_BEER)
                                .with(httpBasic("user", "password"))
                                                .accept(MediaType.APPLICATION_JSON)
                                                .content(objectMapper.writeValueAsString(mockedBeers.get(1)))
                                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isCreated())
                                .andExpect(header().exists("location"));

        }

        @SneakyThrows
        @Test
        void testListBeers() {
                given(beerService.listBeers(null, null, null, null, null)).willReturn(new PageImpl<>(mockedBeers));

                mockMvc.perform(get(BeerController.API_V1_BEER)
                                .with(httpBasic("user", "password"))
                                .accept(MediaType.APPLICATION_JSON))
                                // Assert
                                .andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                // jsonpath documentation : https://github.com/json-path/JsonPath
                                .andExpect(jsonPath("$.totalElements", is(3)));
        }

        @SneakyThrows
        @Test
        void testGetBeerById() {

                // Get Mocked Beer
                BeerDTO beer = mockedBeers.get(0);
                // Set up behaviours and mocks
                given(beerService.getBeerById(any(UUID.class))).willReturn(Optional.of(beer));

                // Act
                mockMvc.perform(get(BeerController.API_V1_BEER_ID_PATH, beer.getId())
                                .with(httpBasic("user", "password"))
                                .accept(MediaType.APPLICATION_JSON))
                                // Assert
                                .andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                // jsonpath documentation : https://github.com/json-path/JsonPath
                                .andExpect(jsonPath("$.id", is(beer.getId().toString())))
                                .andExpect(jsonPath("$.beerName", is(beer.getBeerName())));

        }

        @SneakyThrows
        @Test
        void testNoBeerException() {
                // Set up behaviours and mocks
                UUID beerId = UUID.randomUUID();
                // Set up behaviours and mocks
                given(beerService.getBeerById(any(UUID.class))).willThrow(new NoBeerFoundException(beerId.toString()));
                // Act
                mockMvc.perform(get(BeerController.API_V1_BEER_ID_PATH, beerId.toString())
                                .with(httpBasic("user", "password")))
                                .andExpect(status().isNotFound())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                // jsonpath documentation : https://github.com/json-path/JsonPath
                                .andExpect(jsonPath("$.message", is("Beer with id: " + beerId + " not found")))
                                .andExpect(jsonPath("$.details", is("uri=/api/v1/beer/" + beerId)));
        }

        @Test
        void testValidationException() throws Exception {
                // Set up behaviours and mocks
                when(beerService.getBeerById(any(UUID.class)))
                                .thenThrow(ValidationException.class);

                // Act
                mockMvc.perform(get(BeerController.API_V1_BEER_ID_PATH, UUID.randomUUID())
                                .with(httpBasic("user", "password")))
                                .andExpect(status().is4xxClientError())
                                .andDo(MockMvcResultHandlers.print());
        }

        @Test
        void testConstraintViolationException1() throws Exception {
                BeerDTO beer = BeerDTO.builder().beerName("").build();
                mockMvc.perform(
                                post(BeerController.API_V1_BEER)
                                                .with(httpBasic("user", "password"))
                                                .accept(MediaType.APPLICATION_JSON)
                                                .content(objectMapper.writeValueAsString(beer))
                                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.length()", is(3)));
        }

        @Test
        void testConstraintViolationException2() throws Exception {
                BeerDTO beer = BeerDTO.builder().beerName("").build();
                mockMvc.perform(
                                post(BeerController.API_V1_BEER)
                                                .with(httpBasic("user", "password"))
                                                .accept(MediaType.APPLICATION_JSON)
                                                .content(objectMapper.writeValueAsString(beer))
                                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.length()", is(3)));
        }
}
