package org.example.routeplaner.infrastructure.persistence.repository;

import org.example.common.domain.entity.Day;
import org.example.routeplaner.domain.model.aggregate.TransportationType;
import org.example.routeplaner.infrastructure.persistence.entity.TransportationEntity;
import org.example.routeplaner.infrastructure.persistence.entity.TransportationTypeEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface TransportationEntityRepository extends JpaRepository<TransportationEntity, UUID> {

    List<TransportationEntity> findTransportationEntitiesByFromLocation_NameLikeOrToLocation_NameLike(String fromLocationName, String toLocationName, Pageable pageable);

    long countByFromLocation_NameLikeOrToLocation_NameLike(String fromLocationName, String toLocationName);
}
