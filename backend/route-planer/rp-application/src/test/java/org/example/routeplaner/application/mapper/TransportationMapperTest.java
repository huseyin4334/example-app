package org.example.routeplaner.application.mapper;

import org.example.common.domain.entity.Day;
import org.example.routeplaner.application.dto.command.transport.TransportationCreateCommand;
import org.example.routeplaner.application.dto.response.route.TransportDto;
import org.example.routeplaner.application.dto.response.transportation.TransportationResponse;
import org.example.routeplaner.domain.model.aggregate.Location;
import org.example.routeplaner.domain.model.aggregate.Transportation;
import org.example.routeplaner.domain.model.aggregate.TransportationType;
import org.example.routeplaner.domain.ports.output.repository.LocationRepository;
import org.example.routeplaner.domain.ports.output.repository.TransportationRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class TransportationMapperTest {

    @Test
    void testToDomain() {
        LocationRepository locationRepository = Mockito.mock(LocationRepository.class);
        TransportationRepository transportationRepository = Mockito.mock(TransportationRepository.class);
        TransportationMapper mapper = new TransportationMapper(locationRepository, transportationRepository);

        UUID originId = UUID.randomUUID();
        UUID destId = UUID.randomUUID();
        Location origin = new Location();
        origin.setId(originId);
        Location dest = new Location();
        dest.setId(destId);
        
        TransportationType transportationType = new TransportationType();
        transportationType.setName("BUS");

        when(locationRepository.findById(originId)).thenReturn(origin);
        when(locationRepository.findById(destId)).thenReturn(dest);
        when(transportationRepository.getTransportationTypeById(1L)).thenReturn(transportationType);

        TransportationCreateCommand command = Mockito.mock(TransportationCreateCommand.class);
        when(command.getOrigin()).thenReturn(originId);
        when(command.getDestination()).thenReturn(destId);
        when(command.getTransportationType()).thenReturn(1L);
        when(command.getAvailableDays()).thenReturn(List.of(Day.MONDAY, Day.FRIDAY));

        Transportation t = mapper.toDomain(command);
        assertEquals(origin, t.getOrigin());
        assertEquals(dest, t.getDestination());
        assertEquals(transportationType, t.getTransportationType());
        assertEquals(List.of(Day.MONDAY, Day.FRIDAY), t.getAvailableDays());
    }

    @Test
    void testToTransportDtoAndResponse() {
        Location origin = new Location();
        origin.setId(UUID.randomUUID());
        origin.setName("Origin");
        Location dest = new Location();
        dest.setId(UUID.randomUUID());
        dest.setName("Dest");
        Transportation t = new Transportation();
        t.setId(UUID.randomUUID());
        t.setOrigin(origin);
        t.setDestination(dest);
        
        TransportationType transportationType = new TransportationType();
        transportationType.setName("UBER");
        t.setTransportationType(transportationType);
        t.setAvailableDays(Arrays.asList(Day.MONDAY, Day.FRIDAY));

        LocationRepository locationRepository = Mockito.mock(LocationRepository.class);
        TransportationRepository transportationRepository = Mockito.mock(TransportationRepository.class);
        TransportationMapper mapper = new TransportationMapper(locationRepository, transportationRepository);

        TransportDto dto = mapper.toTransportDto(t);
        assertEquals("Origin", dto.getOrigin());
        assertEquals("Dest", dto.getDestination());
        assertEquals("UBER", dto.getTransportType());

        TransportationResponse resp = mapper.toTransportResponse(t);
        assertEquals(t.getId().toString(), resp.getId());
        assertEquals("Origin", resp.getOrigin().getName());
        assertEquals("Dest", resp.getDestination().getName());
        assertEquals(Arrays.asList("MONDAY", "FRIDAY"), resp.getAvailableDays());
    }
}
