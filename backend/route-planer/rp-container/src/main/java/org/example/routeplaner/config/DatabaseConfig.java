package org.example.routeplaner.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = "org.example.routeplaner.infrastructure.persistence.repository")
@EntityScan(basePackages = "org.example.routeplaner.infrastructure.persistence.entity")
@Configuration
public class DatabaseConfig {
}
