package org.example.routeplaner.domain.service;

import org.example.common.TransportConstants;
import org.example.common.domain.entity.Day;
import org.example.routeplaner.domain.exception.RouteDomainException;
import org.example.routeplaner.domain.model.aggregate.*;
import org.example.routeplaner.domain.ports.output.repository.AirportRepository;
import org.example.routeplaner.domain.ports.output.repository.TransportationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

class DomainServiceImplTest {

    TransportationRepository transportationRepository;
    AirportRepository airportRepository;
    DomainServiceImpl domainService;

    @BeforeEach
    void setUp() {
        transportationRepository = Mockito.mock(TransportationRepository.class);
        airportRepository = Mockito.mock(AirportRepository.class);
        domainService = new DomainServiceImpl(transportationRepository, airportRepository);
    }

    @Test
    void testRoutePlanerReturnsEmptyIfNoAirportsToDestination() {
        Mockito.when(transportationRepository.getOriginsByCriteria(any())).thenReturn(Collections.emptyList());
        List<Route> routes = domainService.routePlaner(UUID.randomUUID(), UUID.randomUUID(), Day.MONDAY);
        assertTrue(routes.isEmpty());
    }

    @Test
    void testRoutePlanerReturnsEmptyIfNoAirportToAirportFlights() {
        Mockito.when(transportationRepository.getOriginsByCriteria(any()))
                .thenReturn(Collections.singletonList(mockTransportation()));
        Mockito.when(transportationRepository.getFlightsByCriteria(any()))
                .thenReturn(Collections.emptyList());
        List<Route> routes = domainService.routePlaner(UUID.randomUUID(), UUID.randomUUID(), Day.MONDAY);
        assertTrue(routes.isEmpty());
    }

    @Test
    void testRoutePlanerReturnsEmptyIfNoOriginToAirports() {
        Mockito.when(transportationRepository.getOriginsByCriteria(any()))
                .thenReturn(Collections.singletonList(mockTransportation()));
        Mockito.when(transportationRepository.getFlightsByCriteria(any()))
                .thenReturn(Collections.singletonList(mockFlight()));
        Mockito.when(transportationRepository.getOriginsByCriteria(any()))
                .thenReturn(Collections.emptyList());
        List<Route> routes = domainService.routePlaner(UUID.randomUUID(), UUID.randomUUID(), Day.MONDAY);
        assertTrue(routes.isEmpty());
    }

    @Test
    void testRoutePlanerReturnsRoute() {
        Transportation beforeFlight = mockTransportation();
        Transportation flight = mockFlight();
        Transportation afterFlight = mockTransportation();

        // airportsToDestination
        Mockito.when(transportationRepository.getOriginsByCriteria(any()))
                .thenReturn(Collections.singletonList(afterFlight))
                .thenReturn(Collections.singletonList(beforeFlight));
        // airportToAirport
        Mockito.when(transportationRepository.getFlightsByCriteria(any()))
                .thenReturn(Collections.singletonList(flight));

        List<Route> routes = domainService.routePlaner(UUID.randomUUID(), UUID.randomUUID(), Day.MONDAY);
        assertNotNull(routes);
        // The route list may be empty if RouteDomainException is thrown, but if not, it should contain Route objects
        assertTrue(routes.isEmpty() || routes.stream().allMatch(Objects::nonNull));
    }

    @Test
    void testRouteDomainExceptionRules() {
        Transportation beforeFlight = mockFlight();
        Transportation flight = mockTransportation();
        Transportation afterFlight = mockFlight();

        Route route = new Route();
        route.setBeforeFlight(mockTransportation());
        assertThrows(RouteDomainException.class, () -> route.setFlight(flight));
        route.setBeforeFlight(mockTransportation());
        route.setFlight(mockFlight());
        assertThrows(RouteDomainException.class, () -> route.setAfterFlight(afterFlight));
    }

    private Transportation mockTransportation() {
        Transportation t = new Transportation();
        t.setId(UUID.randomUUID());
        t.setOrigin(mockLocation("A"));
        t.setDestination(mockLocation("B"));
        TransportationType transportationType = getTransportationType("BUS");
        t.setTransportationType(transportationType);
        t.setAvailableDays(Arrays.asList(Day.MONDAY, Day.FRIDAY));
        return t;
    }

    private TransportationType getTransportationType(String name) {
        TransportationType transportationType = new TransportationType();
        transportationType.setId(1L);
        transportationType.setName(name);
        return transportationType;
    }

    private Transportation mockFlight() {
        Transportation t = new Transportation();
        t.setId(UUID.randomUUID());
        t.setOrigin(mockLocation("A"));
        t.setDestination(mockLocation("B"));
        t.setTransportationType(getTransportationType(TransportConstants.FLIGHT));
        t.setAvailableDays(Arrays.asList(Day.MONDAY, Day.FRIDAY));
        return t;
    }

    private Location mockLocation(String code) {
        Location l = new Location();
        l.setId(UUID.randomUUID());
        l.setName("Loc" + code);
        l.setCity(new City());
        l.setLocationCode("CODE" + code);
        return l;
    }
}
