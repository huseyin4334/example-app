package org.example.routeplaner.application.dto.response.location;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.common.application.BaseResponse;

import java.util.List;

@AllArgsConstructor
@Getter @Setter
public class CountryResponse implements BaseResponse {
    private long id;
    private String name;
    private List<CityResponse> cities;
}
