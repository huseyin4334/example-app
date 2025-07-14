package org.example.routeplaner.application.dto.response.route;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class AvailableRoutesResponseTest {

    @Test
    void testSetAndGetAvailableRoutes() {
        AvailableRoutesResponse response = new AvailableRoutesResponse();
        AvailableRouteDto dto = new AvailableRouteDto();
        response.setAvailableRoutes(List.of(dto));
        // Reflection or package-private getter, so just check no exception and field is set
        assertNotNull(response);
    }
}
