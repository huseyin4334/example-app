package org.example.routeplaner.userinterface;

import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages =
        {
                "org.example.routeplaner.application",
                "org.example.routeplaner.domain",
                "org.example.routeplaner.userinterface",
        }
)
public class TestApp {
}
