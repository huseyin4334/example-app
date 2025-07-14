package org.example.routeplaner.userinterface.route;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.routeplaner.application.dto.command.route.AvailableRoutesQuery;
import org.example.routeplaner.application.dto.response.route.AvailableRoutesResponse;
import org.example.routeplaner.application.ports.input.RouteApplicationService;
import org.example.routeplaner.domain.exception.RouteDomainException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RouteController.class)
@AutoConfigureMockMvc
@SuppressWarnings("unchecked")
class RouteControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    RouteApplicationService routeApplicationService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void testGetAvailableRoutes() throws Exception {
        AvailableRoutesQuery query = mock(AvailableRoutesQuery.class);
        when(query.getDestinationId()).thenReturn(UUID.randomUUID().toString());
        when(query.getOriginId()).thenReturn(UUID.randomUUID().toString());
        when(query.getSelectedDate()).thenReturn(new Date());
        AvailableRoutesResponse response = new AvailableRoutesResponse();
        when(routeApplicationService.getRouteForTransportation(any(AvailableRoutesQuery.class))).thenReturn(response);

        mockMvc.perform(post("/routes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(query)))
                .andExpect(status().isOk());
        verify(routeApplicationService).getRouteForTransportation(any(AvailableRoutesQuery.class));
    }

    @Test
    void testGetAvailableRoutesThrowsDomainException() throws Exception {
        doThrow(new RouteDomainException("domain error")).when(routeApplicationService).getRouteForTransportation(any());
        AvailableRoutesQuery query = mock(AvailableRoutesQuery.class);
        when(query.getDestinationId()).thenReturn(UUID.randomUUID().toString());
        when(query.getOriginId()).thenReturn(UUID.randomUUID().toString());
        when(query.getSelectedDate()).thenReturn(new Date());

        mockMvc.perform(post("/routes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(query)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message[0]").value("domain error"));
    }

    @Test
    void testGetAvailableRoutesThrowsGenericException() throws Exception {
        doThrow(new RuntimeException("generic error")).when(routeApplicationService).getRouteForTransportation(any());
        AvailableRoutesQuery query = mock(AvailableRoutesQuery.class);
        when(query.getDestinationId()).thenReturn(UUID.randomUUID().toString());
        when(query.getOriginId()).thenReturn(UUID.randomUUID().toString());
        when(query.getSelectedDate()).thenReturn(new Date());

        mockMvc.perform(post("/routes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(query)))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.message[0]").value("generic error"));
    }
}
