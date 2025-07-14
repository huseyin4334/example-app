package org.example.routeplaner.userinterface.route;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.routeplaner.application.dto.command.route.AvailableRoutesQuery;
import org.example.routeplaner.application.dto.response.route.AvailableRoutesResponse;
import org.example.routeplaner.application.ports.input.RouteApplicationService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@Validated
@RequestMapping("/routes")
@RestController
public class RouteController {

    private final RouteApplicationService routeApplicationService;

    @PostMapping
    public ResponseEntity<AvailableRoutesResponse> getAvailableRoutes(
            @Valid @RequestBody AvailableRoutesQuery command
    ) {
        return ResponseEntity.ok().body(routeApplicationService.getRouteForTransportation(command));
    }
}
