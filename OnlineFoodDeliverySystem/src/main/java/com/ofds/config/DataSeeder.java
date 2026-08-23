package com.ofds.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner seedCatalog() {
        return args -> {
            // Intentionally left empty: no startup demo catalog is seeded.
        };
    }
}
