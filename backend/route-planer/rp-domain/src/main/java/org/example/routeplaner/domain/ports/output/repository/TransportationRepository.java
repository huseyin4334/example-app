package org.example.routeplaner.domain.ports.output.repository;

import org.example.routeplaner.domain.model.aggregate.Location;
import org.example.routeplaner.domain.model.aggregate.Transportation;
import org.example.routeplaner.domain.model.aggregate.TransportationType;
import org.example.routeplaner.domain.model.criteria.LocationCriteria;

import java.util.List;
import java.util.UUID;

public interface TransportationRepository extends BaseRepository<Transportation, UUID> {
    List<Transportation> getOriginsByCriteria(LocationCriteria criteria);
    List<Transportation> getDestinationsByCriteria(LocationCriteria locationCriteria);
    List<Transportation> getFlightsByCriteria(LocationCriteria locationCriteria);
    TransportationType getTransportationTypeById(Long id);
}
