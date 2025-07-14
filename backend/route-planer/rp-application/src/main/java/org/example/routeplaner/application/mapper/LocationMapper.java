package org.example.routeplaner.application.mapper;

import lombok.RequiredArgsConstructor;
import org.example.routeplaner.application.dto.command.location.LocationCreateCommand;
import org.example.routeplaner.application.dto.response.location.LocationResponse;
import org.example.routeplaner.domain.model.aggregate.Location;
import org.example.routeplaner.domain.ports.output.repository.RegionRepository;
import org.springframework.stereotype.Component;

import java.util.UUID;

@RequiredArgsConstructor
@Component
public class LocationMapper {
    private final RegionRepository regionRepository;

    public Location toDomain(LocationCreateCommand command) {
        if (command == null) {
            return null;
        }
        Location location = new Location();
        location.setId(
                command.getId() != null ?
                        UUID.fromString(command.getId()) : null
        );
        location.setName(command.getName());
        location.setCity(regionRepository.findCityById(command.getCity()));
        location.setLocationCode(command.getLocationCode());
        return location;
    }

    public LocationResponse toResponse(Location domain) {
        if (domain == null) {
            return null;
        }
        LocationResponse response = new LocationResponse();
        response.setId(domain.getId());
        response.setName(domain.getName());
        response.setCountryCode(domain.getCity().getCountry().getCode());
        response.setCountry(domain.getCity().getCountry().getName());
        response.setCity(domain.getCity().getName());
        response.setLocationCode(domain.getLocationCode());
        return response;
    }
}
