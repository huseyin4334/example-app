package org.example.routeplaner.application.dto.response.location;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.common.application.BaseResponse;

import java.util.UUID;

@AllArgsConstructor
@Getter @Setter
public class SearchableLocationResponse implements BaseResponse {
    private UUID id;
    private String name;
}
