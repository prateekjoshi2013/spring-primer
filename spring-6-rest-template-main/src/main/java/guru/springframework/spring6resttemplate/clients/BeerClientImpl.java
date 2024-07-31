package guru.springframework.spring6resttemplate.clients;

import java.net.URI;
import java.util.Map;
import java.util.UUID;

import org.apache.catalina.connector.Response;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.JsonNode;

import guru.springframework.spring6resttemplate.model.BeerDTO;
import guru.springframework.spring6resttemplate.model.BeerDTOPageImpl;
import guru.springframework.spring6resttemplate.model.BeerStyle;
import guru.springframework.spring6resttemplate.model.CustomPageImpl;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BeerClientImpl implements BeerClient {

        public static final String GET_BEER_PATH = "/api/v1/beer";
        public static final String GET_BEER_BY_ID_PATH = "/api/v1/beer/{beerId}";
        private final RestTemplateBuilder restTemplateBuilder;

        /**
         * {
         * "totalPages": 2410,
         * "totalElements": 2410,
         * "size": 1,
         * "content": [
         * {
         * "id": "294cab03-f97d-49b0-8edd-8c699c9bc17f",
         * "version": 0,
         * "beerName": "#001 Golden Amber Lager",
         * "beerStyle": "PILSNER",
         * "upc": "2382",
         * "quantityOnHand": null,
         * "price": 10.00,
         * "createdDate": "2024-07-24T19:35:48.906501",
         * "updateDate": "2024-07-24T19:35:48.906502"
         * }
         * ],
         * "number": 0,
         * "sort": {
         * "empty": false,
         * "sorted": true,
         * "unsorted": false
         * },
         * "pageable": {
         * "pageNumber": 0,
         * "pageSize": 1,
         * "sort": {
         * "empty": false,
         * "sorted": true,
         * "unsorted": false
         * },
         * "offset": 0,
         * "paged": true,
         * "unpaged": false
         * },
         * "numberOfElements": 1,
         * "first": true,
         * "last": false,
         * "empty": false
         * }
         * 
         * 
         */
        @Override
        public Page<BeerDTO> listBeers() {
                RestTemplate restTemplate = restTemplateBuilder.build();
                // create a uri by appending to basepath we set in rest template builder
                UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromPath(GET_BEER_PATH);

                // Alternate ways of fetching object
                // ResponseEntity<String> stringResponse =
                // restTemplate.getForEntity(uriComponentsBuilder.toUriString(),
                // String.class);
                // ResponseEntity<Map> mapResponse =
                // restTemplate.getForEntity(uriComponentsBuilder.toUriString(),
                // Map.class);

                // ResponseEntity<JsonNode> jsonResponse =
                // restTemplate.getForEntity(uriComponentsBuilder.toUriString(),
                // JsonNode.class);
                // jsonResponse.getBody().findPath("content").elements().forEachRemaining(node
                // -> {
                // System.out.println(node.get("beerName").asText());
                // });
                // ResponseEntity<BeerDTOPageImpl> beerDtoResponse = restTemplate.getForEntity(
                // uriComponentsBuilder.toUriString(),
                // BeerDTOPageImpl.class);

                ResponseEntity<CustomPageImpl<BeerDTO>> responseEntity = restTemplate.exchange(
                                uriComponentsBuilder.toUriString(),
                                HttpMethod.GET,
                                null,
                                new ParameterizedTypeReference<CustomPageImpl<BeerDTO>>() {
                                });

                return responseEntity.getBody();
        }

        @Override
        public Page<BeerDTO> listBeers(String beerName, BeerStyle beerStyle, Boolean showInventory, Integer pageNumber,
                        Integer pageSize) {
                RestTemplate restTemplate = restTemplateBuilder.build();
                // create a uri by appending to basepath we set in rest template builder
                UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromPath(GET_BEER_PATH);
                // set query param
                uriComponentsBuilder.queryParam("beerName", beerName);
                uriComponentsBuilder.queryParam("beerStyle", beerStyle);
                uriComponentsBuilder.queryParam("showInventory", showInventory);
                uriComponentsBuilder.queryParam("pageNumber", pageNumber);
                uriComponentsBuilder.queryParam("pageSize", pageSize);
                ResponseEntity<CustomPageImpl<BeerDTO>> responseEntity = restTemplate.exchange(
                                uriComponentsBuilder.toUriString(),
                                HttpMethod.GET,
                                null,
                                new ParameterizedTypeReference<CustomPageImpl<BeerDTO>>() {
                                });

                return responseEntity.getBody();
        }

        @Override
        public BeerDTO getBeerById(UUID beerId) {
                RestTemplate restTemplate = restTemplateBuilder.build();
                return restTemplate.getForObject(GET_BEER_BY_ID_PATH, BeerDTO.class, beerId);
        }

        @Override
        public BeerDTO createBeer(BeerDTO newDto) {
                RestTemplate restTemplate = restTemplateBuilder.build();
                ResponseEntity<BeerDTO> response = restTemplate.postForEntity(GET_BEER_PATH,
                                newDto, BeerDTO.class);
                return response.getBody();
                // if you only have Location header attribute populated use the below logic
                // URI uri = restTemplate.postForLocation(GET_BEER_PATH, newDto);
                // return restTemplate.getForObject(uri, BeerDTO.class);
        }

        @Override
        public BeerDTO updateBeer(BeerDTO beerDto) {
                RestTemplate restTemplate = restTemplateBuilder.build();
                restTemplate.put(GET_BEER_BY_ID_PATH,
                                beerDto, beerDto.getId());
                return this.getBeerById(beerDto.getId());
        }

        @Override
        public void deleteBeer(UUID id) {
                RestTemplate restTemplate = restTemplateBuilder.build();
                restTemplate.delete(GET_BEER_BY_ID_PATH, id);
        }

}
