package org.example.routeplaner.application.ports.output;

import org.example.routeplaner.application.dto.response.transportation.TransportationTypeResponse;

import java.util.List;

public interface TransformationApplicationRepository {
    List<TransportationTypeResponse> getTransformationTypes();
}
