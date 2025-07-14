package org.example.routeplaner.userinterface.location;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.common.application.BaseListResponse;
import org.example.routeplaner.application.dto.command.DeleteCommand;
import org.example.routeplaner.application.dto.command.GetQuery;
import org.example.routeplaner.application.dto.command.SearchQuery;
import org.example.routeplaner.application.dto.command.location.LocationCreateCommand;
import org.example.routeplaner.application.dto.response.location.CountryResponse;
import org.example.routeplaner.application.dto.response.location.LocationResponse;
import org.example.routeplaner.application.dto.response.location.SearchableLocationResponse;
import org.example.routeplaner.application.ports.input.LocationApplicationService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Validated
@RequestMapping("/locations")
@RequiredArgsConstructor
@RestController
public class LocationController {
    private final LocationApplicationService locationApplicationService;


    @PutMapping
    public ResponseEntity<Void> createLocation(@Valid @RequestBody LocationCreateCommand command) {
        locationApplicationService.create(command);
        return ResponseEntity.ok().build();
    }


    @PostMapping
    public ResponseEntity<Void> updateLocation(@Valid @RequestBody LocationCreateCommand command) {
        locationApplicationService.update(command);
        return ResponseEntity.ok().build();
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocation(
            @PathVariable String id
    ) {
        locationApplicationService.delete(new DeleteCommand<>(UUID.fromString(id)));
        return ResponseEntity.ok().build();
    }


    @GetMapping("/{id}")
    public ResponseEntity<LocationResponse> getLocation(
            @PathVariable String id
    ) {
        // Assuming a method exists in the service to fetch a location by ID
        LocationResponse location = locationApplicationService.findById(new GetQuery<>(UUID.fromString(id)));
        return ResponseEntity.ok(location);
    }


    @PostMapping("/page")
    public ResponseEntity<BaseListResponse<LocationResponse>> searchLocation(@RequestBody SearchQuery searchQuery) {
        BaseListResponse<LocationResponse> location = locationApplicationService.list(searchQuery);
        return ResponseEntity.ok(location);
    }

    @GetMapping("/all")
    public ResponseEntity<BaseListResponse<SearchableLocationResponse>> getAllLocations() {
        BaseListResponse<SearchableLocationResponse> locations = locationApplicationService.getAllSearchableLocations();
        return ResponseEntity.ok(locations);
    }


    @GetMapping("cities")
    public ResponseEntity<BaseListResponse<CountryResponse>> getAllCities() {
        BaseListResponse<CountryResponse> cities = locationApplicationService.getAllCities();
        return ResponseEntity.ok(cities);
    }
}
