package guru.springframework.spring6resttemplate.clients;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import guru.springframework.spring6resttemplate.model.BeerStyle;

@SpringBootTest
public class BeerClientImplTest {
    @Autowired
    BeerClient beerClientImpl;

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
}
