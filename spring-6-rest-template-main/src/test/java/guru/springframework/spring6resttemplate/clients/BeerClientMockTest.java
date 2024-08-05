package guru.springframework.spring6resttemplate.clients;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.DynamicTest.stream;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.queryParam;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestToUriTemplate;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withAccepted;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withNoContent;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.math.BigDecimal;
import java.net.URI;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.MockServerRestClientCustomizer;
import org.springframework.boot.test.web.client.MockServerRestTemplateCustomizer;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

import guru.springframework.spring6resttemplate.config.OAuthClientInterceptor;
import guru.springframework.spring6resttemplate.config.RestTemplateBuilderConfig;
import guru.springframework.spring6resttemplate.model.BeerDTO;
import guru.springframework.spring6resttemplate.model.BeerStyle;
import guru.springframework.spring6resttemplate.model.CustomPageImpl;
import lombok.SneakyThrows;

@RestClientTest
@Import(RestTemplateBuilderConfig.class)
public class BeerClientMockTest {
    private static final String AUTH_TOKEN = "Bearer test";

    private static final String URL = "http://localhost:8080";

    BeerClient beerClient;

    MockRestServiceServer server;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    RestTemplateBuilder restTemplateBuilderConfigured;

    @Mock
    RestTemplateBuilder mockRestTemplateBuilder = new RestTemplateBuilder(new MockServerRestTemplateCustomizer());

    @MockBean
    OAuth2AuthorizedClientManager oAuth2AuthorizedClientManager;

    @Autowired
    ClientRegistrationRepository clientRegistrationRepository;

    @TestConfiguration
    public static class TestConfig {
        @Bean
        ClientRegistrationRepository clientRegistrationRepository() {
            return new InMemoryClientRegistrationRepository(
                    ClientRegistration.withRegistrationId("springauth")
                            .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                            .clientId("test")
                            .tokenUri("test")
                            .build());
        }

        @Bean
        OAuth2AuthorizedClientService auth2AuthorizedClientService(
                ClientRegistrationRepository clientRegistrationRepository) {
            return new InMemoryOAuth2AuthorizedClientService(clientRegistrationRepository);
        }

        @Bean
        OAuthClientInterceptor oAuthClientInterceptor(OAuth2AuthorizedClientManager manager,
                ClientRegistrationRepository clientRegistrationRepository) {
            return new OAuthClientInterceptor(manager, clientRegistrationRepository);
        }
    }

    @BeforeEach
    void setUp() {

        ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId("springauth");

        OAuth2AccessToken token = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER,
                "test", Instant.MIN, Instant.MAX);

        when(oAuth2AuthorizedClientManager.authorize(any())).thenReturn(new OAuth2AuthorizedClient(clientRegistration,
                "test", token));

        RestTemplate restTemplate = restTemplateBuilderConfigured.build();
        server = MockRestServiceServer.bindTo(restTemplate).build();
        when(mockRestTemplateBuilder.build()).thenReturn(restTemplate);
        beerClient = new BeerClientImpl(mockRestTemplateBuilder);
    }

    @Test
    @SneakyThrows
    void testDeleteBeerById() {
        BeerDTO beerDto = getBeerDto();
        UUID id = beerDto.getId();

        server.expect(method(HttpMethod.DELETE))
                .andExpect(header("Authorization", AUTH_TOKEN))
                .andExpect(requestToUriTemplate(URL + BeerClientImpl.GET_BEER_BY_ID_PATH, id))
                .andExpect(header("Authorization", AUTH_TOKEN))
                .andRespond(withNoContent());

        beerClient.deleteBeer(id);
        server.verify();
    }

    @Test
    @SneakyThrows
    void testUpdateBeer() {
        BeerDTO beerDto = getBeerDto();
        UUID id = beerDto.getId();
        beerDto.setVersion(1);

        BeerDTO beerDtoUpdated = BeerDTO.builder()
                .id(id)
                .beerName("Updated Beer Name")
                .beerStyle(beerDto.getBeerStyle())
                .version(2)
                .price(beerDto.getPrice())
                .quantityOnHand(beerDto.getQuantityOnHand())
                .upc(beerDto.getUpc()).build();

        String updatedPayload = objectMapper.writeValueAsString(beerDtoUpdated);

        server
                .expect(requestToUriTemplate(URL + BeerClientImpl.GET_BEER_BY_ID_PATH, id))
                .andExpect(header("Authorization", AUTH_TOKEN))
                .andExpect(method(HttpMethod.PUT))
                .andRespond(withSuccess(updatedPayload, MediaType.APPLICATION_JSON));

        server
                .expect(requestToUriTemplate(URL + BeerClientImpl.GET_BEER_BY_ID_PATH, id))
                .andExpect(method(HttpMethod.GET))
                .andExpect(header("Authorization", AUTH_TOKEN))
                .andRespond(withSuccess(updatedPayload, MediaType.APPLICATION_JSON));

        BeerDTO updatedDtos = beerClient.updateBeer(beerDto);
        assertThat(beerDto.getVersion() + 1).isEqualTo(updatedDtos.getVersion());
        server.verify();
    }

    @Test
    @SneakyThrows
    void testCreateBeer() {
        BeerDTO dto = getBeerDto();
        String payload = objectMapper.writeValueAsString(dto);

        server.expect(method(HttpMethod.POST))
                .andExpect(requestTo(URL + BeerClientImpl.GET_BEER_PATH))
                .andExpect(header("Authorization", AUTH_TOKEN))
                .andRespond(withSuccess(payload, MediaType.APPLICATION_JSON));
        // .andRespond(withAccepted().location(uri)); to check for location field

        BeerDTO dtos = beerClient.createBeer(dto);
        assertThat(dtos.getId()).isEqualTo(dto.getId());
    }

    @Test
    @SneakyThrows
    void testListBeersWithQueryParams() {
        String payload = objectMapper.writeValueAsString(getPage());
        URI uri = UriComponentsBuilder.fromHttpUrl(URL + BeerClientImpl.GET_BEER_PATH)
                .queryParam("beerName", "ALE")
                .queryParam("beerStyle", "ALE")
                .queryParam("showInventory", true)
                .queryParam("pageNumber", 1)
                .queryParam("pageSize", 1)
                .build().toUri();
        server.expect(method(HttpMethod.GET))
                .andExpect(header("Authorization", AUTH_TOKEN))
                .andExpect(requestTo(uri))
                .andRespond(withSuccess(payload, MediaType.APPLICATION_JSON));

        Page<BeerDTO> dtos = beerClient.listBeers("ALE", BeerStyle.ALE, true, 1,
                1);
        assertThat(dtos.getContent().size()).isGreaterThan(0);
    }

    @Test
    @SneakyThrows
    void testListBeers() {
        String payload = objectMapper.writeValueAsString(getPage());
        server.expect(method(HttpMethod.GET))
                .andExpect(header("Authorization", AUTH_TOKEN))
                .andExpect(requestTo(URL + BeerClientImpl.GET_BEER_PATH))
                .andRespond(withSuccess(payload, MediaType.APPLICATION_JSON));

        Page<BeerDTO> dtos = beerClient.listBeers();
        assertThat(dtos.getContent().size()).isGreaterThan(0);
    }

    @Test
    @SneakyThrows
    void testGetBeerById() {
        BeerDTO beerDto = getBeerDto();
        UUID id = beerDto.getId();
        String payload = objectMapper.writeValueAsString(beerDto);

        server.expect(method(HttpMethod.GET))
                .andExpect(header("Authorization", AUTH_TOKEN))
                .andExpect(requestToUriTemplate(URL + BeerClientImpl.GET_BEER_BY_ID_PATH, id))
                .andRespond(withSuccess(payload, MediaType.APPLICATION_JSON));
        ;

        BeerDTO dtos = beerClient.getBeerById(id);
        assertEquals(dtos.getId(), id);
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
