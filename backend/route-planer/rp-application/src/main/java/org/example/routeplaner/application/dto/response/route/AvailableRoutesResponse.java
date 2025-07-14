package org.example.routeplaner.application.dto.response.route;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AvailableRoutesResponse {
    private List<AvailableRouteDto> availableRoutes;
}
