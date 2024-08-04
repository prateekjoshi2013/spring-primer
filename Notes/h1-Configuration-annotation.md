- The @Configuration annotation is typically used in conjunction with the @Bean annotation to define beans that will be managed by the Spring container. 
- This approach is part of Spring's Java-based configuration support, allowing you to configure your Spring application using Java code rather than XML.

```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public MyService myService() {
        return new MyServiceImpl();
    }

    @Bean
    public MyRepository myRepository() {
        return new MyRepositoryImpl();
    }
}
```