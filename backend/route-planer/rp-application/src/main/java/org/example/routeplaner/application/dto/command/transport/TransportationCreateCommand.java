package org.example.routeplaner.application.dto.command.transport;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.example.common.application.BaseCommand;
import org.example.common.domain.entity.Day;

import java.util.List;
import java.util.UUID;

@Setter
@Getter
public class TransportationCreateCommand implements BaseCommand {
    private UUID id;

    @NotNull(message = "Origin cannot be null")
    private UUID origin;

    @NotNull(message = "Destination cannot be null")
    private UUID destination;

    @NotNull(message = "Transportation type cannot be null")
    private Long transportationType;

    @NotEmpty(message = "Available days cannot be empty")
    private List<Day> availableDays;
}
