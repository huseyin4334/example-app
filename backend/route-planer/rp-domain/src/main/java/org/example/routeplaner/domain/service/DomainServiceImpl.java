package org.example.routeplaner.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.common.domain.entity.Day;
import org.example.routeplaner.domain.exception.RouteDomainException;
import org.example.routeplaner.domain.model.aggregate.Location;
import org.example.routeplaner.domain.model.aggregate.Route;
import org.example.routeplaner.domain.model.aggregate.Transportation;
import org.example.routeplaner.domain.model.aggregate.TransportationType;
import org.example.routeplaner.domain.model.criteria.LocationCriteria;
import org.example.routeplaner.domain.ports.output.repository.TransportationRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class DomainServiceImpl implements DomainService {

    private final TransportationRepository transportationRepository;

    @Override
    public List<Route> routePlaner(
            UUID origin,
            UUID destination,
            Day availableDay
    ) {
        // 1-Get airports that have transportation to the destination
        // 2-Get airports that have flights to the destination connected airports
        // 2.1-Also considering origin and available days. Because founded airports should have transportation by origin into available days.

        List<Transportation> airportsToDestination = getAirportToDestinationTransportations(destination, availableDay);

        if (airportsToDestination.isEmpty()) {
            log.warn("No airports found with transportation to destination: {}", destination);
            return List.of();
        }

        List<Transportation> airportToAirpost = getAirportToAirportFlights(origin, availableDay, airportsToDestination);

        if (airportToAirpost.isEmpty()) {
            log.warn("No flights found from origin: {} to destination: {}", origin, destination);
            return List.of();
        }

        List<Transportation> originToAirports = getOriginToAirportsTransportations(availableDay, airportToAirpost, origin);

        if (originToAirports.isEmpty()) {
            log.warn("No transportations found from origin: {} to airports", origin);
            return List.of();
        }

        return getRoutesByTransportations(airportToAirpost, originToAirports, airportsToDestination);
    }

    private List<Route> getRoutesByTransportations(List<Transportation> airportToAirpost, List<Transportation> originToAirports, List<Transportation> airportsToDestination) {
        List<Route> rotes = new ArrayList<>();
        for (Transportation transportation : airportToAirpost) {
            List<Transportation> filteredOriginToAirports = originToAirports.stream()
                    .filter(t -> t.getDestination().getId().equals(transportation.getOrigin().getId()))
                    .toList();

            List<Transportation> filteredAirportsToDestination = airportsToDestination.stream()
                    .filter(t -> t.getOrigin().getId().equals(transportation.getDestination().getId()))
                    .toList();

            for (Transportation originToAirport : filteredOriginToAirports) {
                for (Transportation airportToDest : filteredAirportsToDestination) {
                    try {
                        Route route = new Route();
                        route.setBeforeFlight(originToAirport);
                        route.setFlight(transportation);
                        route.setAfterFlight(airportToDest);
                        rotes.add(route);
                    } catch (RouteDomainException e) {
                        log.warn("Error validating route: {}", e.getMessage());
                    }
                }
            }
        }
        return rotes;
    }

    private List<Transportation> getOriginToAirportsTransportations(Day availableDay, List<Transportation> airportToAirpost, UUID origin) {
        LocationCriteria locationCriteria = new LocationCriteria();
        locationCriteria.setDestinationIdList(
                airportToAirpost.stream()
                        .map(t -> t.getOrigin().getId())
                        .collect(Collectors.toList())
        );
        locationCriteria.setOriginIdList(List.of(origin));
        locationCriteria.setExcludedTransportationType("FLIGHT");
        locationCriteria.setAvailableDay(availableDay);

        return transportationRepository.getOriginsByCriteria(locationCriteria);
    }

    private List<Transportation> getAirportToAirportFlights(UUID origin, Day availableDay, List<Transportation> airportsToDestination) {
        LocationCriteria locationCriteria = new LocationCriteria();
        locationCriteria.setDestinationIdList(
                airportsToDestination.stream()
                        .map(t -> t.getOrigin().getId())
                        .collect(Collectors.toList())
        );
        locationCriteria.setTransportationType("FLIGHT");
        locationCriteria.setRootOriginId(origin);
        locationCriteria.setAvailableDay(availableDay);

        // We got flights that have transportations by origin into available days.
        return transportationRepository.getFlightsByCriteria(locationCriteria);
    }

    private List<Transportation> getAirportToDestinationTransportations(UUID destination, Day availableDay) {
        LocationCriteria locationCriteria = new LocationCriteria();
        locationCriteria.setDestinationIdList(List.of(destination));
        locationCriteria.setExcludedTransportationType("FLIGHT");
        locationCriteria.setAvailableDay(availableDay);

        return transportationRepository.getOriginsByCriteria(locationCriteria);
    }
}
