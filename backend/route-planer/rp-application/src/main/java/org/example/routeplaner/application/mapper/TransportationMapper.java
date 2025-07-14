package org.example.routeplaner.application.mapper;

import lombok.RequiredArgsConstructor;
import org.example.routeplaner.application.dto.command.transport.TransportationCreateCommand;
import org.example.routeplaner.application.dto.command.transport.TransportationUpdateCommand;
import org.example.routeplaner.application.dto.response.route.TransportDto;
import org.example.routeplaner.application.dto.response.transportation.TransportLocationDto;
import org.example.routeplaner.application.dto.response.transportation.TransportationResponse;
import org.example.routeplaner.application.dto.response.transportation.TransportationTypeResponse;
import org.example.routeplaner.domain.model.aggregate.Transportation;
import org.example.routeplaner.domain.model.aggregate.TransportationType;
import org.example.routeplaner.domain.ports.output.repository.LocationRepository;
import org.example.routeplaner.domain.ports.output.repository.TransportationRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class TransportationMapper {

    private final LocationRepository locationRepository;
    private final TransportationRepository transportationRepository;

    public Transportation toDomain(TransportationCreateCommand command) {
        Transportation transportation = new Transportation();
        transportation.setOrigin(locationRepository.findById(command.getOrigin()));
        transportation.setDestination(locationRepository.findById(command.getDestination()));
        transportation.setTransportationType(transportationRepository.getTransportationTypeById(command.getTransportationType()));
        transportation.setAvailableDays(command.getAvailableDays());
        return transportation;
    }

    public Transportation toDomain(TransportationUpdateCommand command) {
        Transportation transportation = new Transportation();
        transportation.setId(command.getId());
        transportation.setTransportationType(transportationRepository.getTransportationTypeById(command.getTransportationType()));
        transportation.setAvailableDays(command.getAvailableDays());
        return transportation;
    }

    public TransportDto toTransportDto(Transportation domain) {
        if (domain == null) {
            return null;
        }

        TransportDto dto = new TransportDto();
        dto.setOrigin(domain.getOrigin().getName());
        dto.setDestination(domain.getDestination().getName());
        dto.setTransportType(domain.getTransportationType().getName());
        return dto;
    }

    public TransportationResponse toTransportResponse(Transportation domain) {
        if (domain == null) {
            return null;
        }

        TransportationResponse response = new TransportationResponse();
        response.setId(domain.getId().toString());
        response.setOrigin(
                new TransportLocationDto(
                        domain.getOrigin().getId().toString(),
                        domain.getOrigin().getName()
                )
        );
        response.setDestination(
                new TransportLocationDto(
                        domain.getDestination().getId().toString(),
                        domain.getDestination().getName()
                )
        );
        response.setTransportType(domain.getTransportationType().getName());
        response.setAvailableDays(domain.getAvailableDays().stream().map(Enum::name).toList());
        return response;
    }
}
