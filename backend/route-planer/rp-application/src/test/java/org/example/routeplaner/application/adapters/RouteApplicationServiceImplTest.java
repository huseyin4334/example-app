package org.example.routeplaner.application.adapters;

import org.example.common.domain.entity.Day;
import org.example.routeplaner.application.dto.command.route.AvailableRoutesQuery;
import org.example.routeplaner.application.dto.response.route.AvailableRouteDto;
import org.example.routeplaner.application.dto.response.route.AvailableRoutesResponse;
import org.example.routeplaner.application.mapper.RouteMapper;
import org.example.routeplaner.domain.model.aggregate.Route;
import org.example.routeplaner.domain.service.DomainService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RouteApplicationServiceImplTest {

    private DomainService domainService;
    private RouteMapper routeMapper;
    private RouteApplicationServiceImpl service;

    @BeforeEach
    void setUp() {
        domainService = mock(DomainService.class);
        routeMapper = mock(RouteMapper.class);
        service = new RouteApplicationServiceImpl(domainService, routeMapper);
    }

    @Test
    void testGetRouteForTransportation() {
        AvailableRoutesQuery query = mock(AvailableRoutesQuery.class);
        UUID originId = UUID.randomUUID();
        UUID destId = UUID.randomUUID();
        Date date = new Date();
        when(query.getOriginId()).thenReturn(originId.toString());
        when(query.getDestinationId()).thenReturn(destId.toString());
        when(query.getSelectedDate()).thenReturn(date);

        Route route = new Route();
        AvailableRouteDto dto = new AvailableRouteDto();
        when(domainService.routePlaner(any(), any(), any())).thenReturn(List.of(route));
        when(routeMapper.toAvailableRoute(route)).thenReturn(dto);

        AvailableRoutesResponse response = service.getRouteForTransportation(query);

        assertNotNull(response);
        assertEquals(dto, response.getAvailableRoutes().get(0));
    }

    @Test
    void testDateToDay() throws Exception {
        // Use reflection to access private method
        var method = RouteApplicationServiceImpl.class.getDeclaredMethod("dateToDay", Date.class);
        method.setAccessible(true);

        LocalDate localDate = LocalDate.of(2023, 6, 5); // MONDAY
        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Day day = (Day) method.invoke(service, date);
        assertEquals(Day.MONDAY, day);

        assertNull(method.invoke(service, (Object) null));
    }
}
