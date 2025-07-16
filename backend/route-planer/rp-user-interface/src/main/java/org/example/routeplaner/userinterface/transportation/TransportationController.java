package org.example.routeplaner.userinterface.transportation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.common.application.BaseListResponse;
import org.example.routeplaner.application.dto.command.DeleteCommand;
import org.example.routeplaner.application.dto.command.GetQuery;
import org.example.routeplaner.application.dto.command.SearchQuery;
import org.example.routeplaner.application.dto.command.transport.TransportationCreateCommand;
import org.example.routeplaner.application.dto.command.transport.TransportationUpdateCommand;
import org.example.routeplaner.application.dto.response.transportation.TransportationResponse;
import org.example.routeplaner.application.dto.response.transportation.TransportationTypeResponse;
import org.example.routeplaner.application.ports.input.TransportationApplicationService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Validated
@RequestMapping("/transportations")
@RequiredArgsConstructor
@RestController
public class TransportationController {
    private final TransportationApplicationService transportationApplicationService;


    @PutMapping
    public ResponseEntity<Void> createTransportation(@Valid @RequestBody TransportationCreateCommand command) {
        transportationApplicationService.create(command);
        return ResponseEntity.ok().build();
    }


    @PostMapping
    public ResponseEntity<Void> updateTransportation(@Valid @RequestBody TransportationUpdateCommand command) {
        transportationApplicationService.update(command);
        return ResponseEntity.ok().build();
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransportation(
            @PathVariable String id
    ) {
        transportationApplicationService.delete(new DeleteCommand<>(UUID.fromString(id)));
        return ResponseEntity.ok().build();
    }


    @GetMapping("/{id}")
    public ResponseEntity<TransportationResponse> getTransportation(
            @PathVariable String id
    ) {
        // Assuming a method exists in the service to fetch a location by ID
        TransportationResponse location = transportationApplicationService.findById(new GetQuery<>(UUID.fromString(id)));
        return ResponseEntity.ok(location);
    }


    @PostMapping("/page")
    public ResponseEntity<BaseListResponse<TransportationResponse>> searchTransportation(@RequestBody SearchQuery searchQuery) {
        BaseListResponse<TransportationResponse> location = transportationApplicationService.list(searchQuery);
        return ResponseEntity.ok(location);
    }

    @GetMapping("/available-types")
    public ResponseEntity<BaseListResponse<TransportationTypeResponse>> getAvailableTransportationTypes() {
        return ResponseEntity.ok(transportationApplicationService.getAvailableTransportationTypes());
    }
}
