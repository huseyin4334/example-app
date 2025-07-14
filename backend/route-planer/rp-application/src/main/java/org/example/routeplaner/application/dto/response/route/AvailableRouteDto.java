package org.example.routeplaner.application.dto.response.route;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AvailableRouteDto {
    private TransportDto beforeFlight;
    private TransportDto flight;
    private TransportDto afterFlight;
}
