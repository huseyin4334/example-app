package org.example.routeplaner.domain.ports.output.repository;

import org.example.routeplaner.domain.model.aggregate.Location;

import java.util.UUID;

public interface LocationRepository extends BaseRepository<Location, UUID> {
}
