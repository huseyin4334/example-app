package org.example.routeplaner.infrastructure.persistence;

import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages =
        {
                "org.example.routeplaner.infrastructure.persistence",
                "org.example.routeplaner.application",
                "org.example.routeplaner.domain",
        }
)
public class TestApplication {
}
