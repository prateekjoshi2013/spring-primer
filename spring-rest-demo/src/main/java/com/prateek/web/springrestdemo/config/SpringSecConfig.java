package com.prateek.web.springrestdemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SpringSecConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http)throws Exception{
        http
        // Enable HTTP Basic authentication with default settings
        .httpBasic(withDefaults())
        // Configure CSRF to ignore specific paths
        .csrf(csrf -> csrf
            .ignoringRequestMatchers("/api/**") // Paths to ignore CSRF protection
        )
        // Authorize requests
        .authorizeHttpRequests(authorizeRequests -> 
            authorizeRequests
                .anyRequest().authenticated() // Require authentication for all requests
        );

        return http.build();

    }
    
}
