package guru.springframework.spring6resttemplate.clients;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BeerClientImplTest {
    @Autowired
    BeerClient beerClientImpl;

    @Test
    void testListBeers() {
        beerClientImpl.listBeers();
    }
}
