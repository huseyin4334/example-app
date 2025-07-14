package org.example.routeplaner.application.ports.input;

import org.example.common.application.BaseListResponse;
import org.example.common.application.BaseResponse;
import org.example.routeplaner.application.dto.command.DeleteCommand;
import org.example.routeplaner.application.dto.command.GetQuery;
import org.example.routeplaner.application.dto.command.SearchQuery;

public interface BasePageService<ID, RESPONSE extends BaseResponse> {
    /**
     * Lists items based on the provided page query.
     *
     * @param command the page query containing pagination and search parameters
     */
    BaseListResponse<RESPONSE> list(SearchQuery command);

    /**
     * Finds an item by its ID.
     *
     * @param command the command containing the details of the item to be created
     */
     RESPONSE findById(GetQuery<ID> command);

    /**
     * Delete an item based on the provided command.
     *
     * @param command the command containing the details of the item to be deleted
     */
    void delete(DeleteCommand<ID> command);
}
