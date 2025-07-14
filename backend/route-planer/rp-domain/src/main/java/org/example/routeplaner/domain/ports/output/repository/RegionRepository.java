package org.example.routeplaner.domain.ports.output.repository;

import org.example.routeplaner.domain.model.aggregate.City;

public interface RegionRepository {
    City findCityById(Long id);
}
