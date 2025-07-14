package org.example.routeplaner.application.dto.response.transportation;

import lombok.Getter;
import lombok.Setter;
import org.example.common.application.BaseResponse;

import java.util.List;

@Getter @Setter
public class TransportationResponse implements BaseResponse {
    private String id;
    private TransportLocationDto origin;
    private TransportLocationDto destination;
    private String transportType;
    private List<String> availableDays;
}
