package org.example.routeplaner.domain.model.aggregate;

import lombok.Getter;
import lombok.Setter;
import org.example.common.domain.entity.AggregateRoot;

import java.util.UUID;

@Getter @Setter
public class Location extends AggregateRoot<UUID> {
    private String name;
    private City city;
    private String locationCode;

    public void isValid() {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Location name must not be null or empty.");
        }
        if (city == null || city.getId() == null) {
            throw new IllegalArgumentException("City must not be null.");
        }
        if (locationCode == null || locationCode.isBlank()) {
            throw new IllegalArgumentException("Location code must not be null or empty.");
        }
    }
}
