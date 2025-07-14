package org.example.routeplaner.infrastructure.persistence.adapter;

import lombok.RequiredArgsConstructor;
import org.example.routeplaner.domain.model.aggregate.City;
import org.example.routeplaner.domain.ports.output.repository.RegionRepository;
import org.example.routeplaner.infrastructure.persistence.mapper.RegionEntitiesMapper;
import org.example.routeplaner.infrastructure.persistence.repository.CityEntityRepository;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class RegionRepositoryImpl implements RegionRepository {

    private final CityEntityRepository cityEntityRepository;
    private final RegionEntitiesMapper regionEntitiesMapper;

    @Override
    public City findCityById(Long id) {
        if (id == null) {
            return null;
        }
        return regionEntitiesMapper.toCity(
                cityEntityRepository.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("City not found with id: " + id))
        );
    }
}
