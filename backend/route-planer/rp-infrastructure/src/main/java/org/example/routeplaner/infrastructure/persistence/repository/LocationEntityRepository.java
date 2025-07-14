package org.example.routeplaner.infrastructure.persistence.repository;

import org.example.routeplaner.infrastructure.persistence.entity.LocationEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface LocationEntityRepository extends JpaRepository<LocationEntity, UUID> {
    List<LocationEntity> findByNameLikeOrLocationCodeLike(String name, String locationCode, Pageable pageable);

    long countByNameLikeOrLocationCodeLike(String name, String locationCode);
}
