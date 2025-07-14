package org.example.routeplaner.domain.model.criteria;

import lombok.Getter;
import lombok.Setter;
import org.example.common.domain.entity.Day;
import org.example.routeplaner.domain.model.aggregate.TransportationType;

import java.util.List;
import java.util.UUID;

@Getter @Setter
public class LocationCriteria {
    private List<UUID> originIdList;
    private List<UUID> destinationIdList;
    private UUID rootOriginId;

    private String transportationType;
    private String excludedTransportationType;

    private Day availableDay;
}
