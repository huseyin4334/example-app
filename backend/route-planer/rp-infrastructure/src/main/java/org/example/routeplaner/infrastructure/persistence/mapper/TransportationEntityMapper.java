package org.example.routeplaner.infrastructure.persistence.mapper;

import lombok.RequiredArgsConstructor;
import org.example.routeplaner.domain.model.aggregate.Transportation;
import org.example.routeplaner.domain.model.aggregate.TransportationType;
import org.example.routeplaner.infrastructure.persistence.entity.TransportationEntity;
import org.example.routeplaner.infrastructure.persistence.entity.TransportationTypeEntity;
import org.example.routeplaner.infrastructure.persistence.repository.TransportationTypeEntityRepository;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class TransportationEntityMapper {
    private final LocationEntityMapper locationEntityMapper;

    public TransportationEntity toTransportationEntity(Transportation transportation) {
        if (transportation == null) {
            return null;
        }
        TransportationEntity transportationEntity = new TransportationEntity();
        transportationEntity.setId(transportation.getId());
        transportationEntity.setFromLocation(locationEntityMapper.referenceById(transportation.getOrigin().getId()));
        transportationEntity.setToLocation(locationEntityMapper.referenceById(transportation.getDestination().getId()));
        transportationEntity.setTransportationType(referenceTransportationTypeEntity(transportation.getTransportationType().getId()));
        transportationEntity.setAvailableDays(transportation.getAvailableDays());
        return transportationEntity;
    }

    public TransportationEntity toTransportationEntityForUpdate(Transportation transportation) {
        if (transportation == null) {
            return null;
        }
        TransportationEntity transportationEntity = new TransportationEntity();
        transportationEntity.setId(transportation.getId());
        transportationEntity.setTransportationType(referenceTransportationTypeEntity(transportation.getTransportationType().getId()));
        transportationEntity.setAvailableDays(transportation.getAvailableDays());
        return transportationEntity;
    }


    public Transportation toTransportation(TransportationEntity transportationEntity) {
        if (transportationEntity == null) {
            return null;
        }
        Transportation transportation = new Transportation();
        transportation.setId(transportationEntity.getId());
        transportation.setOrigin(locationEntityMapper.toLocation(transportationEntity.getFromLocation()));
        transportation.setDestination(locationEntityMapper.toLocation(transportationEntity.getToLocation()));
        transportation.setTransportationType(toTransportationType(transportationEntity.getTransportationType()));
        transportation.setAvailableDays(transportationEntity.getAvailableDays());
        return transportation;
    }

    public TransportationType toTransportationType(TransportationTypeEntity transportationTypeEntity) {
        if (transportationTypeEntity == null) {
            return null;
        }
        TransportationType transportationType = new TransportationType();
        transportationType.setId(transportationTypeEntity.getId());
        transportationType.setName(transportationTypeEntity.getName());
        return transportationType;
    }

    public TransportationTypeEntity referenceTransportationTypeEntity(Long id) {
        if (id == null) {
            return null;
        }
        TransportationTypeEntity transportationType = new TransportationTypeEntity();
        transportationType.setId(id);
        return transportationType;
    }
}
