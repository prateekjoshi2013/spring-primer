package guru.springframework.spring6resttemplate.clients;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.stream;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;

import com.fasterxml.jackson.databind.ObjectMapper;

import guru.springframework.spring6resttemplate.model.BeerDTO;
import guru.springframework.spring6resttemplate.model.BeerStyle;
import guru.springframework.spring6resttemplate.model.CustomPageImpl;
import lombok.SneakyThrows;

@RestClientTest(BeerClientImpl.class)
public class BeerClientMockTest {
    private static final String URL = "http://localhost:8080";
    @Autowired
    BeerClient beerClient;

    @Autowired
    MockRestServiceServer server;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @SneakyThrows
    void testListBeers() {
        String payload = objectMapper.writeValueAsString(getPage());
        server.expect(method(HttpMethod.GET))
                .andExpect(requestTo(URL + BeerClientImpl.GET_BEER_PATH))
                .andRespond(withSuccess(payload, MediaType.APPLICATION_JSON));
        ;

        Page<BeerDTO> dtos = beerClient.listBeers();
        assertThat(dtos.getContent().size()).isGreaterThan(0);
    }

    BeerDTO getBeerDto() {
        return BeerDTO.builder().id(UUID.randomUUID())
                .price(BigDecimal.TEN)
                .beerName("Mango Bobs")
                .beerStyle(BeerStyle.IPA)
                .quantityOnHand(500)
                .upc("123456")
                .build();
    }

    CustomPageImpl<BeerDTO> getPage() {
        List<BeerDTO> list = Stream.of(getBeerDto()).toList();
        return new CustomPageImpl<BeerDTO>(list, PageRequest.of(1, list.size(), Sort.unsorted()), 1);
    }

}
