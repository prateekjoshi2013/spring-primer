package guru.springframework.spring6resttemplate.clients;

import java.util.UUID;

import org.springframework.data.domain.Page;

import guru.springframework.spring6resttemplate.model.BeerDTO;
import guru.springframework.spring6resttemplate.model.BeerStyle;

public interface BeerClient {
    Page<BeerDTO> listBeers();

    Page<BeerDTO> listBeers(String beerName, BeerStyle beerStyle, Boolean showInventory, Integer pageNumber,
            Integer pageSize);

    BeerDTO getBeerById(UUID id);

    BeerDTO createBeer(BeerDTO newDto);
}
