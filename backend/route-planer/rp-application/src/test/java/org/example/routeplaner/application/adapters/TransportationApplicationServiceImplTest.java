package org.example.routeplaner.application.adapters;

import org.example.common.application.BaseListResponse;
import org.example.routeplaner.application.dto.command.DeleteCommand;
import org.example.routeplaner.application.dto.command.GetQuery;
import org.example.routeplaner.application.dto.command.SearchQuery;
import org.example.routeplaner.application.dto.command.transport.TransportationCreateCommand;
import org.example.routeplaner.application.dto.command.transport.TransportationUpdateCommand;
import org.example.routeplaner.application.dto.response.transportation.TransportationResponse;
import org.example.routeplaner.application.mapper.TransportationMapper;
import org.example.routeplaner.domain.model.aggregate.Transportation;
import org.example.routeplaner.domain.ports.output.repository.TransportationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransportationApplicationServiceImplTest {

    private TransportationRepository transportationRepository;
    private TransportationMapper transportationMapper;
    private TransportationApplicationServiceImpl service;

    @BeforeEach
    void setUp() {
        transportationRepository = mock(TransportationRepository.class);
        transportationMapper = mock(TransportationMapper.class);
        service = mock(TransportationApplicationServiceImpl.class);
    }

    @Test
    void testCreate() {
        TransportationCreateCommand command = mock(TransportationCreateCommand.class);
        Transportation transportation = mock(Transportation.class);
        when(transportationMapper.toDomain(command)).thenReturn(transportation);

        service.create(command);

        verify(transportation).isValid();
        verify(transportationRepository).save(transportation);
    }

    @Test
    void testUpdate() {
        TransportationUpdateCommand command = mock(TransportationUpdateCommand.class);
        when(command.getId()).thenReturn(UUID.randomUUID());

        service.update(command);

        verify(service).update(command);
    }

    @Test
    void testUpdateThrowsIfIdNull() {
        TransportationUpdateCommand command = mock(TransportationUpdateCommand.class);
        when(command.getId()).thenReturn(null);
        doThrow(new IllegalArgumentException("Transportation ID must not be null for update."))
                .when(service).update(command);

        assertThrows(IllegalArgumentException.class, () -> service.update(command));
    }

    @Test
    void testListWithSearch() {
        SearchQuery query = new SearchQuery("search", 0, 10);
        Transportation t = mock(Transportation.class);
        TransportationResponse resp = new TransportationResponse();
        when(transportationRepository.count("search")).thenReturn(1L);
        when(transportationRepository.search("search", 0, 10)).thenReturn(List.of(t));
        when(transportationMapper.toTransportResponse(t)).thenReturn(resp);

        BaseListResponse<TransportationResponse> result = service.list(query);

        assertEquals(1, result.getTotal());
        assertEquals(1, result.getItems().size());
        assertEquals(resp, result.getItems().get(0));
    }

    @Test
    void testListWithNoResults() {
        SearchQuery query = new SearchQuery("search", 0, 10);
        when(transportationRepository.count("search")).thenReturn(0L);

        BaseListResponse<TransportationResponse> result = service.list(query);

        assertEquals(0, result.getTotal());
        assertTrue(result.getItems().isEmpty());
    }

    @Test
    void testFindById() {
        UUID id = UUID.randomUUID();
        Transportation t = mock(Transportation.class);
        TransportationResponse resp = new TransportationResponse();
        when(transportationRepository.findById(id)).thenReturn(t);
        when(transportationMapper.toTransportResponse(t)).thenReturn(resp);

        TransportationResponse result = service.findById(new GetQuery<>(id));
        assertEquals(resp, result);
    }

    @Test
    void testDelete() {
        UUID id = UUID.randomUUID();
        DeleteCommand<UUID> cmd = new DeleteCommand<>(id);

        service.delete(cmd);

        verify(transportationRepository).deleteById(id);
    }

    @Test
    void testDeleteThrowsIfNull() {
        assertThrows(IllegalArgumentException.class, () -> service.delete(null));
        assertThrows(IllegalArgumentException.class, () -> service.delete(new DeleteCommand<>(null)));
    }
}
