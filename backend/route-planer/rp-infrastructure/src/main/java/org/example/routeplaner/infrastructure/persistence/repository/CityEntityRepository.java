package org.example.routeplaner.infrastructure.persistence.repository;

import org.example.routeplaner.infrastructure.persistence.entity.CityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface CityEntityRepository extends JpaRepository<CityEntity,Long> {

    @Query("SELECT c FROM LocationEntity l LEFT JOIN l.city c WHERE l.id = :locationId")
    CityEntity findByLocationId(UUID locationId);
}
