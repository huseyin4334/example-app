package org.example.routeplaner.application.ports.output;

import org.example.routeplaner.application.dto.response.location.CountryResponse;
import org.example.routeplaner.application.dto.response.location.SearchableLocationResponse;

import java.util.List;

public interface LocationApplicationRepository {
    List<SearchableLocationResponse> getAllSearchableLocations();

    List<CountryResponse> getAllCountries();
}
