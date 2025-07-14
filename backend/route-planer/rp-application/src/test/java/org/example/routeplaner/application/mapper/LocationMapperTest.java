package org.example.routeplaner.application.mapper;

import org.example.routeplaner.application.dto.command.location.LocationCreateCommand;
import org.example.routeplaner.application.dto.response.location.LocationResponse;
import org.example.routeplaner.domain.model.aggregate.City;
import org.example.routeplaner.domain.model.aggregate.Location;
import org.example.routeplaner.domain.ports.output.repository.RegionRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class LocationMapperTest {

    @Test
    void testToDomain() {
        String id = UUID.randomUUID().toString();
        LocationCreateCommand command = new LocationCreateCommand(
                id, "Name", 1L, "CODE"
        );
        
        RegionRepository regionRepository = Mockito.mock(RegionRepository.class);
        City city = new City();
        city.setId(1L);
        city.setName("Test City");
        when(regionRepository.findCityById(1L)).thenReturn(city);
        
        LocationMapper mapper = new LocationMapper(regionRepository);
        Location l = mapper.toDomain(command);
        assertEquals(UUID.fromString(id), l.getId());
        assertEquals("Name", l.getName());
        assertEquals(city, l.getCity());
        assertEquals("CODE", l.getLocationCode());
    }

    @Test
    void testToResponse() {
        Location l = new Location();
        UUID id = UUID.randomUUID();
        l.setId(id);
        l.setName("Name");
        
        City city = new City();
        city.setId(1L);
        city.setName("Test City");
        l.setCity(city);
        l.setLocationCode("CODE");
        
        RegionRepository regionRepository = Mockito.mock(RegionRepository.class);
        LocationMapper mapper = new LocationMapper(regionRepository);
        LocationResponse resp = mapper.toResponse(l);
        assertEquals(id, resp.getId());
        assertEquals("Name", resp.getName());
        assertEquals("CODE", resp.getLocationCode());
    }
}
