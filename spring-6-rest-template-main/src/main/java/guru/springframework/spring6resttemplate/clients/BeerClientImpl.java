package guru.springframework.spring6resttemplate.clients;

import java.util.Map;

import org.apache.catalina.connector.Response;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;

import guru.springframework.spring6resttemplate.model.BeerDTO;
import guru.springframework.spring6resttemplate.model.BeerDTOPageImpl;
import guru.springframework.spring6resttemplate.model.CustomPageImpl;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BeerClientImpl implements BeerClient {

    private static final String BASE_URL = "http://localhost:8080";
    private static final String GET_BEER_PATH = "/api/v1/beer";
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
        ResponseEntity<String> stringResponse = restTemplate.getForEntity(BASE_URL + GET_BEER_PATH,
                String.class);
        ResponseEntity<Map> mapResponse = restTemplate.getForEntity(BASE_URL + GET_BEER_PATH, Map.class);

        ResponseEntity<JsonNode> jsonResponse = restTemplate.getForEntity(BASE_URL + GET_BEER_PATH, JsonNode.class);
        jsonResponse.getBody().findPath("content").elements().forEachRemaining(node -> {
            System.out.println(node.get("beerName").asText());
        });
        ResponseEntity<BeerDTOPageImpl> beerDtoResponse = restTemplate.getForEntity(BASE_URL + GET_BEER_PATH,
                BeerDTOPageImpl.class);

        ResponseEntity<CustomPageImpl<BeerDTO>> responseEntity = restTemplate.exchange(
                BASE_URL + GET_BEER_PATH,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<CustomPageImpl<BeerDTO>>() {
                });

        return null;
    }

}
