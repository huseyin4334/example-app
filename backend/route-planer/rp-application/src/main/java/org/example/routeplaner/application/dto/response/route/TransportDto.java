package org.example.routeplaner.application.dto.response.route;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TransportDto {
    private String origin;
    private String destination;
    private String transportType;
}
