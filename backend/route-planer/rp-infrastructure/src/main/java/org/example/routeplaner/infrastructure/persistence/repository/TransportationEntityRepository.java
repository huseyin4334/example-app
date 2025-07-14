package org.example.routeplaner.infrastructure.persistence.repository;

import org.example.routeplaner.infrastructure.persistence.entity.TransportationEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TransportationEntityRepository extends JpaRepository<TransportationEntity, UUID> {

    List<TransportationEntity> findTransportationEntitiesByFromLocation_NameLikeOrToLocation_NameLike(String fromLocationName, String toLocationName, Pageable pageable);

    long countByFromLocation_NameLikeOrToLocation_NameLike(String fromLocationName, String toLocationName);
}
