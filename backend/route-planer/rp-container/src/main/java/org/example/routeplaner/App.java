package org.example.routeplaner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class App 
{
    public static void main( String[] args )
    {
        // This is the main entry point for the Spring Boot application.
        // The application will start and scan for components, configurations, etc.
        SpringApplication.run(App.class, args);
    }
}
