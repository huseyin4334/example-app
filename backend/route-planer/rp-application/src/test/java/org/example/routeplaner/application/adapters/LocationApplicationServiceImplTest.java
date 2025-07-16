package org.example.routeplaner.application.adapters;

import org.example.common.application.BaseListResponse;
import org.example.routeplaner.application.dto.command.DeleteCommand;
import org.example.routeplaner.application.dto.command.GetQuery;
import org.example.routeplaner.application.dto.command.SearchQuery;
import org.example.routeplaner.application.dto.command.location.LocationCreateCommand;
import org.example.routeplaner.application.dto.response.location.LocationResponse;
import org.example.routeplaner.application.mapper.LocationMapper;
import org.example.routeplaner.application.ports.output.LocationApplicationRepository;
import org.example.routeplaner.domain.model.aggregate.City;
import org.example.routeplaner.domain.model.aggregate.Location;
import org.example.routeplaner.domain.ports.output.repository.LocationRepository;
import org.example.routeplaner.domain.service.DomainService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LocationApplicationServiceImplTest {

    private LocationRepository locationRepository;
    private LocationMapper locationMapper;
    private LocationApplicationRepository locationApplicationRepository;
    private LocationApplicationServiceImpl service;
    private DomainService domainService;

    @BeforeEach
    void setUp() {
        locationRepository = mock(LocationRepository.class);
        locationMapper = mock(LocationMapper.class);
        locationApplicationRepository = mock(LocationApplicationRepository.class);
        domainService = mock(DomainService.class);
        service = new LocationApplicationServiceImpl(locationRepository, locationApplicationRepository, locationMapper, domainService);
    }

    @Test
    void testCreate() {
        LocationCreateCommand command = new LocationCreateCommand("id", "Name", 1L, "CODE");
        Location location = new Location();
        location.setId(UUID.randomUUID());
        location.setName("Name");
        location.setLocationCode("SAW");
        City city = new City();
        city.setId(2L);
        location.setCity(city);
        when(locationMapper.toDomain(command)).thenReturn(location);

        service.create(command);

        verify(locationRepository).save(location);
    }

    @Test
    void testUpdate() {
        LocationCreateCommand command = new LocationCreateCommand(UUID.randomUUID().toString(), "Name", 1L, "CODE");
        Location location = new Location();
        location.setId(UUID.randomUUID());
        location.setName("Name");
        location.setLocationCode("CODE");
        City city = new City();
        city.setId(2L);
        location.setCity(city);
        when(locationMapper.toDomain(command)).thenReturn(location);

        service.update(command);

        verify(locationRepository).update(location);
    }

    @Test
    void testUpdateThrowsIfIdNull() {
        LocationCreateCommand command = new LocationCreateCommand(null, "Name", 1L, "CODE");
        Location location = new Location();
        location.setId(null);
        when(locationMapper.toDomain(command)).thenReturn(location);

        assertThrows(IllegalArgumentException.class, () -> service.update(command));
    }

    @Test
    void testListWithSearch() {
        SearchQuery query = new SearchQuery("search", 0, 10);
        Location loc = new Location();
        LocationResponse resp = new LocationResponse();
        when(locationRepository.count("search")).thenReturn(1L);
        when(locationRepository.search("search", 0, 10)).thenReturn(List.of(loc));
        when(locationMapper.toResponse(loc)).thenReturn(resp);

        BaseListResponse<LocationResponse> result = service.list(query);

        assertEquals(1, result.getTotal());
        assertEquals(1, result.getItems().size());
        assertEquals(resp, result.getItems().get(0));
    }

    @Test
    void testListWithNoResults() {
        SearchQuery query = new SearchQuery("search", 0, 10);
        when(locationRepository.count("search")).thenReturn(0L);

        BaseListResponse<LocationResponse> result = service.list(query);

        assertEquals(0, result.getTotal());
        assertTrue(result.getItems().isEmpty());
    }

    @Test
    void testFindById() {
        UUID id = UUID.randomUUID();
        Location loc = new Location();
        LocationResponse resp = new LocationResponse();
        when(locationRepository.findById(id)).thenReturn(loc);
        when(locationMapper.toResponse(loc)).thenReturn(resp);

        LocationResponse result = service.findById(new GetQuery<>(id));
        assertEquals(resp, result);
    }

    @Test
    void testDelete() {
        UUID id = UUID.randomUUID();
        DeleteCommand<UUID> cmd = new DeleteCommand<>(id);

        service.delete(cmd);

        verify(locationRepository).deleteById(id);
    }

    @Test
    void testDeleteThrowsIfNull() {
        assertThrows(IllegalArgumentException.class, () -> service.delete(null));
        assertThrows(IllegalArgumentException.class, () -> service.delete(new DeleteCommand<>(null)));
    }
}
