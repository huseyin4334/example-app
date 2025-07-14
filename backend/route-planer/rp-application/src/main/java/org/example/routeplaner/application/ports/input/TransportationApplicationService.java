package org.example.routeplaner.application.ports.input;

import org.example.common.application.BaseListResponse;
import org.example.routeplaner.application.dto.command.transport.TransportationCreateCommand;
import org.example.routeplaner.application.dto.command.transport.TransportationUpdateCommand;
import org.example.routeplaner.application.dto.response.transportation.TransportationResponse;
import org.example.routeplaner.application.dto.response.transportation.TransportationTypeResponse;

import java.util.UUID;

public interface TransportationApplicationService extends BasePageService<UUID, TransportationResponse> {
    /**
     * Creates a new transportation based on the provided command.
     *
     * @param command the command containing the details of the transportation to be created
     */
    void create(TransportationCreateCommand command);

    /**
     * Updates an existing transportation based on the provided command.
     *
     * @param command the command containing the details of the transportation to be updated
     */
    void update(TransportationUpdateCommand command);

    BaseListResponse<TransportationTypeResponse> getAvailableTransportationTypes();

}
