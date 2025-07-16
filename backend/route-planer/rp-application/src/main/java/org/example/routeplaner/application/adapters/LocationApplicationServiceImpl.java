package org.example.routeplaner.application.adapters;

import lombok.RequiredArgsConstructor;
import org.example.common.application.BaseListResponse;
import org.example.routeplaner.application.dto.command.DeleteCommand;
import org.example.routeplaner.application.dto.command.GetQuery;
import org.example.routeplaner.application.dto.command.SearchQuery;
import org.example.routeplaner.application.dto.command.location.LocationCreateCommand;
import org.example.routeplaner.application.dto.response.location.CountryResponse;
import org.example.routeplaner.application.dto.response.location.LocationResponse;
import org.example.routeplaner.application.dto.response.location.SearchableLocationResponse;
import org.example.routeplaner.application.mapper.LocationMapper;
import org.example.routeplaner.application.ports.input.LocationApplicationService;
import org.example.routeplaner.application.ports.output.LocationApplicationRepository;
import org.example.routeplaner.domain.model.aggregate.Location;
import org.example.routeplaner.domain.ports.output.repository.LocationRepository;
import org.example.routeplaner.domain.service.DomainService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class LocationApplicationServiceImpl implements LocationApplicationService {
    private final LocationRepository locationRepository;
    private final LocationApplicationRepository locationApplicationRepository;
    private final LocationMapper locationMapper;
    private final DomainService domainService;

    @Transactional
    @Override
    public void create(LocationCreateCommand command) {
        Location location = locationMapper.toDomain(command);
        domainService.initiateLocation(location);
        locationRepository.save(location);
    }

    @Transactional
    @Override
    public void update(LocationCreateCommand command) {
        Location location = locationMapper.toDomain(command);

        if (location.getId() == null) {
            throw new IllegalArgumentException("Location ID must not be null for update.");
        }

        domainService.initiateLocation(location);
        locationRepository.update(location);
    }

    @Override
    public BaseListResponse<SearchableLocationResponse> getAllSearchableLocations() {
        BaseListResponse<SearchableLocationResponse> response = new BaseListResponse<>();
        response.setItems(locationApplicationRepository.getAllSearchableLocations());
        response.setTotal(response.getItems().size());
        response.setPageSize(response.getPageSize());
        response.setPage(0);
        return response;
    }

    @Override
    public BaseListResponse<LocationResponse> list(SearchQuery command) {
        if (command == null) {
            throw new IllegalArgumentException("SearchQuery command must not be null.");
        }

        BaseListResponse<LocationResponse> response = new BaseListResponse<>();

        long count = locationRepository.count(command.getSearchTerm());

        response.setPage(command.getPageNo());
        response.setPageSize(command.getPageSize());
        response.setTotal(count);

        if (count == 0 || ((long) command.getPageNo() * command.getPageSize()) >= count) {
            return response;
        }

        List<Location> locations;
        if (command.getSearchTerm() == null || command.getSearchTerm().isEmpty())
            locations = locationRepository.page(command.getPageNo(), command.getPageSize());
        else
            locations = locationRepository.search(command.getSearchTerm(), command.getPageNo(), command.getPageSize());

        response.setItems(
                locations
                        .stream()
                        .map(locationMapper::toResponse)
                        .toList()
        );

        return response;
    }

    @Override
    public LocationResponse findById(GetQuery<UUID> command) {
        return locationMapper.toResponse(locationRepository.findById(command.getId()));
    }

    @Transactional
    @Override
    public void delete(DeleteCommand<UUID> command) {
        if (command == null || command.getId() == null) {
            throw new IllegalArgumentException("DeleteCommand and its ID must not be null.");
        }
        locationRepository.deleteById(command.getId());
    }

    @Override
    public BaseListResponse<CountryResponse> getAllCities() {
        BaseListResponse<CountryResponse> response = new BaseListResponse<>();
        response.setItems(locationApplicationRepository.getAllCountries());
        response.setTotal(response.getItems().size());
        response.setPageSize(response.getPageSize());
        response.setPage(0);
        return response;
    }
}
