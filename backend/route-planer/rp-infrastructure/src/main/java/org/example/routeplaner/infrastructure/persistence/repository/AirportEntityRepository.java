package org.example.routeplaner.infrastructure.persistence.repository;

import org.example.routeplaner.infrastructure.persistence.entity.AirportEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AirportEntityRepository extends JpaRepository<AirportEntity, Long> {
    boolean existsByCodeAndCity_Id(String airportCode, Long cityId);
}
