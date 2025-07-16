package org.example.routeplaner.domain.ports.output.repository;

import org.example.routeplaner.domain.model.aggregate.City;

public interface AirportRepository {
    boolean isExistAirportByCodeAndCity(String airportCode, City city);
}
