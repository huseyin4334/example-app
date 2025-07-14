package org.example.routeplaner.application.ports.input;

import org.example.routeplaner.application.dto.command.route.AvailableRoutesQuery;
import org.example.routeplaner.application.dto.response.route.AvailableRoutesResponse;

public interface RouteApplicationService {
    /**
     * Retrieves the route for a given transportation.
     *
     * @param availableRoutesQuery the query containing transportation details
     * @return the response containing available routes for the transportation
     */
    AvailableRoutesResponse getRouteForTransportation(AvailableRoutesQuery availableRoutesQuery);
}
