package org.example.routeplaner.infrastructure.persistence.adapter;

import lombok.RequiredArgsConstructor;
import org.example.routeplaner.domain.model.aggregate.City;
import org.example.routeplaner.domain.ports.output.repository.AirportRepository;
import org.example.routeplaner.infrastructure.persistence.repository.AirportEntityRepository;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class AirportRepositoryImpl implements AirportRepository {
    private final AirportEntityRepository airportEntityRepository;

    @Override
    public boolean isExistAirportByCodeAndCity(String airportCode, City city) {
        return airportEntityRepository.existsByCodeAndCity_Id(airportCode, city.getId());
    }
}
