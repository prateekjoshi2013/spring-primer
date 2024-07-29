package guru.springframework.spring6resttemplate.clients;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.web.client.HttpClientErrorException;

import guru.springframework.spring6resttemplate.model.BeerDTO;
import guru.springframework.spring6resttemplate.model.BeerStyle;

@SpringBootTest
public class BeerClientImplTest {
    @Autowired
    BeerClient beerClientImpl;

    @Test
    void testCreateBeer() {
        BeerDTO newDto = BeerDTO.builder()
                .price(BigDecimal.TEN)
                .beerName("Prateek Beer")
                .beerStyle(BeerStyle.IPA)
                .quantityOnHand(500)
                .upc("123456")
                .build();
        BeerDTO savedDto = beerClientImpl.createBeer(newDto);
        assertNotNull(savedDto);
    }

    @Test
    void testUpdateBeer() {
        String newBeerName = "Prateek New Beer";
        BeerDTO newDto = BeerDTO.builder()
                .price(BigDecimal.TEN)
                .beerName("Prateek Beer")
                .beerStyle(BeerStyle.IPA)
                .quantityOnHand(500)
                .upc("123456")
                .build();
        BeerDTO savedDto = beerClientImpl.createBeer(newDto);
        savedDto.setBeerName(newBeerName);
        BeerDTO updatedBeer = beerClientImpl.updateBeer(savedDto);
        assertEquals(updatedBeer.getBeerName(), newBeerName);
    }

    @Test
    void testDeleteBeer() {
        BeerDTO newDto = BeerDTO.builder()
                .price(BigDecimal.TEN)
                .beerName("Prateek Beer")
                .beerStyle(BeerStyle.IPA)
                .quantityOnHand(500)
                .upc("123456")
                .build();
        BeerDTO savedDto = beerClientImpl.createBeer(newDto);
        beerClientImpl.deleteBeer(savedDto.getId());
        assertThrows(HttpClientErrorException.class, () -> {
            beerClientImpl.getBeerById(savedDto.getId());
        });
    }

    @Test
    void testListBeers() {
        beerClientImpl.listBeers();
    }

    @Test
    void testListBeersWithName() {
        assertEquals(beerClientImpl.listBeers("ALE", null, null, null, null).getContent().get(0).getQuantityOnHand(),
                null);
        assertNotEquals(beerClientImpl.listBeers("ALE", BeerStyle.PALE_ALE, true, null, null), null);
    }

    @Test
    void getBeerById() {
        BeerDTO dto = beerClientImpl.listBeers("ALE", BeerStyle.PALE_ALE, true, null, null).getContent().get(0);
        BeerDTO byId = beerClientImpl.getBeerById(dto.getId());
        assertNotNull(byId);

    }
}
