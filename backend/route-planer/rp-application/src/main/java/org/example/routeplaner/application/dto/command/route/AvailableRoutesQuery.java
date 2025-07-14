package org.example.routeplaner.application.dto.command.route;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.example.common.application.BaseCommand;

import java.util.Date;

@Setter
@Getter
public class AvailableRoutesQuery implements BaseCommand {
    @NotEmpty(message = "Origin ID must not be empty")
    private String originId;
    @NotEmpty(message = "Destination ID must not be empty")
    private String destinationId;
    @NotNull(message = "Selected date must not be null")
    private Date selectedDate;
}
