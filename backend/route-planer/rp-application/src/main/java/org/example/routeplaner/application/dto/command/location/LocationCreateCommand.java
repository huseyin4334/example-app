package org.example.routeplaner.application.dto.command.location;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.common.application.BaseCommand;

@AllArgsConstructor
@Getter @Setter
public class LocationCreateCommand implements BaseCommand {

    private String id;

    @Size(min = 5, max = 256, message = "Name must be between 5 and 256 characters long")
    @NotBlank(message = "Name must be between 5 and 256 characters long")
    private String name;

    @NotNull(message = "City ID must not be null")
    private Long city;

    @Size(min = 2, max = 16, message = "Location code must be between 2 and 16 characters long")
    @NotBlank(message = "Location code must be between 2 and 16 characters long")
    private String locationCode;
}
