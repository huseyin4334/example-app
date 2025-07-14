package org.example.routeplaner.application.mapper;

import org.example.routeplaner.application.dto.response.route.AvailableRouteDto;
import org.example.routeplaner.application.dto.response.route.TransportDto;
import org.example.routeplaner.domain.model.aggregate.Location;
import org.example.routeplaner.domain.model.aggregate.Route;
import org.example.routeplaner.domain.model.aggregate.Transportation;
import org.example.routeplaner.domain.model.aggregate.TransportationType;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RouteMapperTest {

    private TransportationType transportationType(String name) {
        TransportationType type = new TransportationType();
        type.setName(name);
        return type;
    }

    @Test
    void testToAvailableRoute() {
        TransportationMapper transportationMapper = Mockito.mock(TransportationMapper.class);
        RouteMapper mapper = new RouteMapper(transportationMapper);

        Route route = new Route();
        Transportation before = new Transportation();
        before.setTransportationType(transportationType("BUS"));
        before.setOrigin(new Location());
        before.setId(UUID.randomUUID());
        Transportation flight = new Transportation();
        flight.setTransportationType(transportationType("FLIGHT"));
        flight.setOrigin(new Location());
        flight.setId(UUID.randomUUID());
        Transportation after = new Transportation();
        after.setTransportationType(transportationType("TRAIN"));
        after.setOrigin(new Location());
        after.setId(UUID.randomUUID());

        route.setBeforeFlight(before);
        route.setFlight(flight);
        route.setAfterFlight(after);

        TransportDto beforeDto = new TransportDto();
        TransportDto flightDto = new TransportDto();
        TransportDto afterDto = new TransportDto();

        Mockito.when(transportationMapper.toTransportDto(before)).thenReturn(beforeDto);
        Mockito.when(transportationMapper.toTransportDto(flight)).thenReturn(flightDto);
        Mockito.when(transportationMapper.toTransportDto(after)).thenReturn(afterDto);

        AvailableRouteDto dto = mapper.toAvailableRoute(route);
        assertEquals(beforeDto, dto.getBeforeFlight());
        assertEquals(flightDto, dto.getFlight());
        assertEquals(afterDto, dto.getAfterFlight());
    }
}
