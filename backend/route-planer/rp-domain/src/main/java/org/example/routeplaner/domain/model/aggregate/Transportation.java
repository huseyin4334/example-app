package org.example.routeplaner.domain.model.aggregate;

import lombok.Getter;
import lombok.Setter;
import org.example.common.domain.entity.AggregateRoot;
import org.example.common.domain.entity.Day;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class Transportation extends AggregateRoot<UUID> {
    private Location origin;
    private Location destination;
    private TransportationType transportationType;

    private List<Day> availableDays;

    public void isValid() {
        if (origin == null || origin.getId() == null) {
            throw new IllegalArgumentException("Origin location must not be null.");
        }
        if (destination == null || destination.getId() == null) {
            throw new IllegalArgumentException("Destination location must not be null.");
        }
        if (transportationType == null || transportationType.getId() == null) {
            throw new IllegalArgumentException("Transportation type must not be null.");
        }
        if (availableDays == null || availableDays.isEmpty()) {
            throw new IllegalArgumentException("Available days must not be null.");
        }

        if (!origin.getCity().getCountry().equals(destination.getCity().getCountry())) {
            throw new IllegalArgumentException("Origin and destination must be in the same country.");
        }
    }
}
