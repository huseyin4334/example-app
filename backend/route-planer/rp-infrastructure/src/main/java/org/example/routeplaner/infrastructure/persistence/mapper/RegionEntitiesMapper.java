package org.example.routeplaner.infrastructure.persistence.mapper;

import org.example.routeplaner.domain.model.aggregate.City;
import org.example.routeplaner.domain.model.aggregate.Country;
import org.example.routeplaner.infrastructure.persistence.entity.CityEntity;
import org.springframework.stereotype.Component;

@Component
public class RegionEntitiesMapper {
    public City toCity(CityEntity cityEntity) {
        if (cityEntity == null) {
            return null;
        }
        City city = new City();
        city.setId(cityEntity.getId());
        city.setName(cityEntity.getName());
        Country country = new Country();
        country.setId(cityEntity.getCountry().getId());
        country.setCode(cityEntity.getCountry().getCode());
        country.setName(cityEntity.getCountry().getName());
        city.setCountry(country);
        return city;
    }

    public CityEntity referenceCity(Long id) {
        if (id == null) {
            return null;
        }
        CityEntity cityEntity = new CityEntity();
        cityEntity.setId(id);
        return cityEntity;
    }
}
