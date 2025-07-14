package org.example.routeplaner.application.mapper;

import lombok.RequiredArgsConstructor;
import org.example.routeplaner.application.dto.response.route.AvailableRouteDto;
import org.example.routeplaner.domain.model.aggregate.Route;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class RouteMapper {

    private final TransportationMapper transportationMapper;

    public AvailableRouteDto toAvailableRoute(Route route) {
        if (route == null) {
            return null;
        }
        AvailableRouteDto dto = new AvailableRouteDto();
        dto.setBeforeFlight(transportationMapper.toTransportDto(route.getBeforeFlight()));
        dto.setFlight(transportationMapper.toTransportDto(route.getFlight()));
        dto.setAfterFlight(transportationMapper.toTransportDto(route.getAfterFlight()));
        return dto;
    }
}
