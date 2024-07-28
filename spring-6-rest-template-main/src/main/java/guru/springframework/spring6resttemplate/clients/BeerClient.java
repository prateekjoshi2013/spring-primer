package guru.springframework.spring6resttemplate.clients;

import org.springframework.data.domain.Page;

import guru.springframework.spring6resttemplate.model.BeerDTO;

public interface BeerClient {
    Page<BeerDTO> listBeers();
}
