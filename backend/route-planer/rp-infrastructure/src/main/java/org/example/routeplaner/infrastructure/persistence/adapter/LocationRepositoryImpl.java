package org.example.routeplaner.infrastructure.persistence.adapter;

import lombok.RequiredArgsConstructor;
import org.example.routeplaner.domain.model.aggregate.Location;
import org.example.routeplaner.domain.ports.output.repository.LocationRepository;
import org.example.routeplaner.infrastructure.persistence.mapper.LocationEntityMapper;
import org.example.routeplaner.infrastructure.persistence.repository.LocationEntityRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Repository
public class LocationRepositoryImpl implements LocationRepository {
    private final LocationEntityRepository locationEntityRepository;
    private final LocationEntityMapper locationEntityMapper;

    @Override
    public List<Location> search(String search, int page, int pageSize) {
        search = "%" + search + "%";
        return locationEntityRepository.findByNameLikeOrLocationCodeLike(
                search, search, PageRequest.of(page, pageSize)
        ).stream().map(locationEntityMapper::toLocation).toList();
    }

    @Override
    public List<Location> page(int page, int size) {
        return locationEntityRepository.findAll(PageRequest.of(page, size))
                .stream().map(locationEntityMapper::toLocation).toList();
    }

    @Override
    public Location findById(UUID uuid) {
        return locationEntityRepository.findById(uuid)
                .map(locationEntityMapper::toLocation)
                .orElse(null);
    }

    @Override
    public void save(Location entity) {
        var locationEntity = locationEntityMapper.toLocationEntity(entity);
        locationEntityRepository.save(locationEntity);
        entity.setId(locationEntity.getId());
    }

    @Override
    public void deleteById(UUID id) {
        locationEntityRepository.deleteById(id);
    }

    @Override
    public void update(Location entity) {
        if (entity.getId() == null)
            throw new IllegalArgumentException("Location ID must not be null for update");

        var locationEntity = locationEntityMapper.toLocationEntity(entity);
        locationEntityRepository.save(locationEntity);
    }

    @Override
    public long count(String search) {
        if (search == null || search.isEmpty()) {
            return locationEntityRepository.count();
        }

        search = "%" + search + "%";
        return locationEntityRepository.countByNameLikeOrLocationCodeLike(
                search, search
        );
    }
}
