package org.example.routeplaner.application.ports.input;

import org.example.common.application.BaseListResponse;
import org.example.routeplaner.application.dto.command.location.LocationCreateCommand;
import org.example.routeplaner.application.dto.response.location.CountryResponse;
import org.example.routeplaner.application.dto.response.location.LocationResponse;
import org.example.routeplaner.application.dto.response.location.SearchableLocationResponse;

import java.util.UUID;

public interface LocationApplicationService extends BasePageService<UUID, LocationResponse> {

    /**
     * Creates a new location based on the provided command.
     *
     * @param command the command containing the details of the location to be created
     */
    void create(LocationCreateCommand command);


    /**
     * Updates an existing location based on the provided command.
     *
     * @param command the command containing the details of the location to be updated
     */
    void update(LocationCreateCommand command);

    BaseListResponse<SearchableLocationResponse> getAllSearchableLocations();

    BaseListResponse<CountryResponse> getAllCities();
}
