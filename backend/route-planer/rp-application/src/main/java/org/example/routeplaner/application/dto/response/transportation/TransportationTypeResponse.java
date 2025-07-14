package org.example.routeplaner.application.dto.response.transportation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.common.application.BaseResponse;

@AllArgsConstructor
@Getter @Setter
public class TransportationTypeResponse implements BaseResponse {
    private Long id;
    private String name;
}
