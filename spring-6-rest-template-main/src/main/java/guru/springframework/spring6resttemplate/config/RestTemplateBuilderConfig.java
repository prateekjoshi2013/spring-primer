package guru.springframework.spring6resttemplate.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.client.RestTemplateBuilderConfigurer;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Configuration
public class RestTemplateBuilderConfig {

    @Value("${rest.template.beer.base.url}")
    private String beerBaseUrl;

    @Value("${rest.template.username}")
    private String username;

    @Value("${rest.template.password}")
    private String password;

    @Bean
    RestTemplateBuilder restTemplateBuilder(RestTemplateBuilderConfigurer configurer) {
        RestTemplateBuilder builder = configurer
                                    // sets up resttemplate builder with spring defaults
                                    .configure(new RestTemplateBuilder())
                                    // enable basic authentication
                                    .basicAuthentication(username, password);
        // Now we will override some base uri settings
        DefaultUriBuilderFactory uriBuilderFactory = new DefaultUriBuilderFactory(beerBaseUrl);
        // set the uri builder factory in the builder and return the builder
        return builder.uriTemplateHandler(uriBuilderFactory);
    }

}
