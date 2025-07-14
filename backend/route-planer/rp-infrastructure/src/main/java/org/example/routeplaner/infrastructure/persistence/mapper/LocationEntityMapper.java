package org.example.routeplaner.infrastructure.persistence.mapper;

import lombok.RequiredArgsConstructor;
import org.example.routeplaner.domain.model.aggregate.Location;
import org.example.routeplaner.infrastructure.persistence.entity.LocationEntity;
import org.example.routeplaner.infrastructure.persistence.repository.CityEntityRepository;
import org.springframework.stereotype.Component;

import java.util.UUID;

@RequiredArgsConstructor
@Component
public class LocationEntityMapper {

    private final CityEntityRepository cityEntityRepository;
    private final RegionEntitiesMapper regionEntitiesMapper;

    public LocationEntity toLocationEntity(Location location) {
        if (location == null) {
            return null;
        }
        LocationEntity locationEntity = new LocationEntity();
        locationEntity.setId(location.getId());
        locationEntity.setName(location.getName());
        locationEntity.setCity(regionEntitiesMapper.referenceCity(location.getCity().getId()));
        locationEntity.setLocationCode(location.getLocationCode());
        return locationEntity;
    }


    public Location toLocation(LocationEntity locationEntity) {
        if (locationEntity == null) {
            return null;
        }
        Location location = new Location();
        location.setId(locationEntity.getId());
        location.setName(locationEntity.getName());
        location.setCity(regionEntitiesMapper.toCity(cityEntityRepository.findByLocationId(locationEntity.getId())));
        location.setLocationCode(locationEntity.getLocationCode());
        return location;
    }


    LocationEntity referenceById(UUID id) {
        if (id == null) {
            return null;
        }
        LocationEntity locationEntity = new LocationEntity();
        locationEntity.setId(id);
        return locationEntity;
    }
}
