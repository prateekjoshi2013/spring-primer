package com.prateek.web.springrestdemo.bootstrap;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import com.prateek.web.springrestdemo.domain.entities.Beer;
import com.prateek.web.springrestdemo.domain.entities.Customer;
import com.prateek.web.springrestdemo.mappers.BeerCsvMapper;
import com.prateek.web.springrestdemo.model.BeerStyle;
import com.prateek.web.springrestdemo.repositories.BeerRepository;
import com.prateek.web.springrestdemo.repositories.CustomerRepository;
import com.prateek.web.springrestdemo.services.BeerCsvService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Component
public class BootstrapData implements CommandLineRunner {

        private BeerRepository beerRepository;
        private CustomerRepository customerRepository;
        private BeerCsvService beerCsvService;
        private BeerCsvMapper beerCsvMapper;
        private static final List<Customer> customers = Stream.of(
                        Customer.builder()
                                        .customerName("John")
                                        .createdDate(LocalDateTime.now())
                                        .lastModifiedDate(LocalDateTime.now())
                                        .build(),
                        Customer.builder()
                                        .customerName("Jane")
                                        .createdDate(LocalDateTime.now())
                                        .lastModifiedDate(LocalDateTime.now())
                                        .build(),
                        Customer.builder()
                                        .customerName("Kane")
                                        .createdDate(LocalDateTime.now())
                                        .lastModifiedDate(LocalDateTime.now())
                                        .build())
                        .collect(Collectors.toList());
        private static final List<Beer> beers = Stream.of(
                        Beer.builder()
                                        .beerName("indian pale ale")
                                        .price(BigDecimal.valueOf(12.023))
                                        .beerStyle(BeerStyle.PALE_ALE)
                                        .quantityOnHand(12)
                                        .upc("upc")
                                        .createdDate(LocalDateTime.now())
                                        .updateDate(LocalDateTime.now())
                                        .build(),
                        Beer.builder()
                                        .beerName("Fosters")
                                        .price(BigDecimal.valueOf(10.023))
                                        .beerStyle(BeerStyle.PILSNER)
                                        .quantityOnHand(10)
                                        .upc("upc")
                                        .createdDate(LocalDateTime.now())
                                        .updateDate(LocalDateTime.now())
                                        .build(),
                        Beer.builder()
                                        .beerName("Kingfisher")
                                        .price(BigDecimal.valueOf(10.023))
                                        .beerStyle(BeerStyle.STOUT)
                                        .quantityOnHand(10)
                                        .upc("upc")
                                        .createdDate(LocalDateTime.now())
                                        .updateDate(LocalDateTime.now())
                                        .build())
                        .collect(Collectors.toList());

        @Override
        public void run(String... args) throws Exception {
                if (beerRepository.count() < 10) {
                        File file = ResourceUtils.getFile("classpath:csvdata/beers.csv");
                        List<Beer> beers = beerCsvService.convertCSV(file).stream()
                                        .map(beerCsv -> {
                                                Beer beer = beerCsvMapper.carToCarDto(beerCsv);
                                                beer.setPrice(BigDecimal.TEN);
                                                return beer;
                                        })
                                        .toList();
                        beerRepository.saveAll(beers);
                }

                if (beerRepository.count() == 0) {
                        beerRepository.saveAll(beers);
                }
                if (customerRepository.count() == 0) {
                        customerRepository.saveAll(customers);
                }
        }

}
