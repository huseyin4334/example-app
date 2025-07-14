package org.example.routeplaner.domain.service;

import org.example.common.domain.entity.Day;
import org.example.routeplaner.domain.exception.RouteDomainException;
import org.example.routeplaner.domain.model.aggregate.Location;
import org.example.routeplaner.domain.model.aggregate.Route;
import org.example.routeplaner.domain.model.aggregate.Transportation;

import java.util.List;
import java.util.UUID;

public interface DomainService {
    List<Route> routePlaner(UUID originId, UUID destinationId, Day availableDay);
}
