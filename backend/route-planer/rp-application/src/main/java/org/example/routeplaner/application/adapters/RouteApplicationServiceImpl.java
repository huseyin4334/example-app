package org.example.routeplaner.application.adapters;

import lombok.RequiredArgsConstructor;
import org.example.common.domain.entity.Day;
import org.example.routeplaner.application.dto.command.route.AvailableRoutesQuery;
import org.example.routeplaner.application.dto.response.route.AvailableRoutesResponse;
import org.example.routeplaner.application.mapper.RouteMapper;
import org.example.routeplaner.application.ports.input.RouteApplicationService;
import org.example.routeplaner.domain.model.aggregate.Route;
import org.example.routeplaner.domain.service.DomainService;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class RouteApplicationServiceImpl implements RouteApplicationService {
    private final DomainService domainService;
    private final RouteMapper routeMapper;

    @Override
    public AvailableRoutesResponse getRouteForTransportation(AvailableRoutesQuery availableRoutesQuery) {
        List<Route> routes = domainService.routePlaner(
            UUID.fromString(availableRoutesQuery.getOriginId()),
            UUID.fromString(availableRoutesQuery.getDestinationId()),
            dateToDay(availableRoutesQuery.getSelectedDate())
        );

        AvailableRoutesResponse response = new AvailableRoutesResponse();
        response.setAvailableRoutes(
            routes.stream()
                .map(routeMapper::toAvailableRoute)
                .toList()
        );

        return response;
    }

    private Day dateToDay(Date date) {
        if (date == null) {
            return null;
        }
        DayOfWeek dayOfWeek = DayOfWeek.from(date.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate());
        return Day.valueOf(dayOfWeek.name());
    }
}
