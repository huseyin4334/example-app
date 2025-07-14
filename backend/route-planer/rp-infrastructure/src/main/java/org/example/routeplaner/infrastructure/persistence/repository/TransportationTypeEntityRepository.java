package org.example.routeplaner.infrastructure.persistence.repository;

import org.example.routeplaner.infrastructure.persistence.entity.TransportationTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransportationTypeEntityRepository extends JpaRepository<TransportationTypeEntity,Long> {

}
