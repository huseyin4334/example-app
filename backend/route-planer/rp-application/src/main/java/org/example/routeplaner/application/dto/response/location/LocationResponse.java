package org.example.routeplaner.application.dto.response.location;

import lombok.Getter;
import lombok.Setter;
import org.example.common.application.BaseResponse;

import java.util.UUID;

@Getter @Setter
public class LocationResponse implements BaseResponse {
    private UUID id;
    private String name;
    private String country;
    private String countryCode;
    private String city;
    private String locationCode;
}
