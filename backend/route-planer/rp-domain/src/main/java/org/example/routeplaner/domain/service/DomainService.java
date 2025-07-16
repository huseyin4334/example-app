package org.example.routeplaner.domain.service;

import org.example.common.domain.entity.Day;
import org.example.routeplaner.domain.model.aggregate.Location;
import org.example.routeplaner.domain.model.aggregate.Route;

import java.util.List;
import java.util.UUID;

public interface DomainService {
    /**
     * Plans a route from origin to destination on a given day.
     *
     * @param originId       the UUID of the origin location
     * @param destinationId  the UUID of the destination location
     * @param availableDay   the day when the route is available
     * @return a list of routes that can be taken from origin to destination
     */
    List<Route> routePlaner(UUID originId, UUID destinationId, Day availableDay);

    /**
     * Initiates a location with the given details.
     *
     * @param location the location to be initiated
     */
    void initiateLocation(Location location);
}
