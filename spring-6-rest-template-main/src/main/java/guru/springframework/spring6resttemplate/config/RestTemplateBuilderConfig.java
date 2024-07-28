package guru.springframework.spring6resttemplate.config;

import org.springframework.boot.autoconfigure.web.client.RestTemplateBuilderConfigurer;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Configuration
public class RestTemplateBuilderConfig {
    private static final String BASE_URL = "http://localhost:8080";

    @Bean
    RestTemplateBuilder restTemplateBuilder(RestTemplateBuilderConfigurer configurer) {
        // sets up resttemplate builder with spring defaults
        RestTemplateBuilder builder = configurer.configure(new RestTemplateBuilder());
        // Now we will override some base uri settings
        DefaultUriBuilderFactory uriBuilderFactory = new DefaultUriBuilderFactory(BASE_URL);
        // set the uri builder factory in the builder and return the builder
        return builder.uriTemplateHandler(uriBuilderFactory);
    }

}
