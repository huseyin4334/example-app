package org.example.routeplaner.userinterface.transportation;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ValidationException;
import org.example.common.application.BaseListResponse;
import org.example.common.domain.entity.Day;
import org.example.routeplaner.application.dto.command.DeleteCommand;
import org.example.routeplaner.application.dto.command.GetQuery;
import org.example.routeplaner.application.dto.command.SearchQuery;
import org.example.routeplaner.application.dto.command.transport.TransportationCreateCommand;
import org.example.routeplaner.application.dto.command.transport.TransportationUpdateCommand;
import org.example.routeplaner.application.dto.response.transportation.TransportationResponse;
import org.example.routeplaner.application.ports.input.TransportationApplicationService;
import org.example.routeplaner.domain.exception.RouteDomainException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransportationController.class)
@SuppressWarnings("unchecked")
class TransportationControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    TransportationApplicationService transportationApplicationService;

    @Autowired
    ObjectMapper objectMapper;

    TransportationCreateCommand validCommand;
    TransportationUpdateCommand validUpdateCommand;

    @BeforeEach
    void setUp() {
        validCommand = mock(TransportationCreateCommand.class);
        when(validCommand.getOrigin()).thenReturn(UUID.randomUUID());
        when(validCommand.getDestination()).thenReturn(UUID.randomUUID());
        when(validCommand.getTransportationType()).thenReturn(1L);
        when(validCommand.getAvailableDays()).thenReturn(List.of(Day.MONDAY, Day.FRIDAY));

        validUpdateCommand = mock(TransportationUpdateCommand.class);
        when(validUpdateCommand.getId()).thenReturn(UUID.randomUUID());
        when(validUpdateCommand.getTransportationType()).thenReturn(1L);
        when(validUpdateCommand.getAvailableDays()).thenReturn(List.of(Day.MONDAY, Day.FRIDAY));
    }

    @Test
    void testCreateTransportation() throws Exception {
        mockMvc.perform(put("/transportations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validCommand)))
                .andExpect(status().isOk());
        verify(transportationApplicationService).create(any(TransportationCreateCommand.class));
    }

    @Test
    void testUpdateTransportation() throws Exception {
        mockMvc.perform(post("/transportations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validUpdateCommand)))
                .andExpect(status().isOk());
        verify(transportationApplicationService).update(any(TransportationUpdateCommand.class));
    }

    @Test
    void testDeleteTransportation() throws Exception {
        UUID id = UUID.randomUUID();
        mockMvc.perform(delete("/transportations/{id}", id))
                .andExpect(status().isOk());
        verify(transportationApplicationService).delete(any(DeleteCommand.class));
    }

    @Test
    void testGetTransportation() throws Exception {
        UUID id = UUID.randomUUID();
        TransportationResponse response = new TransportationResponse();
        response.setId(id.toString());
        when(transportationApplicationService.findById(any(GetQuery.class))).thenReturn(response);

        mockMvc.perform(get("/transportations/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()));
    }

    @Test
    void testSearchTransportation() throws Exception {
        SearchQuery query = new SearchQuery("test", 0, 10);
        BaseListResponse<TransportationResponse> resp = new BaseListResponse<>();
        TransportationResponse t = new TransportationResponse();
        t.setId(UUID.randomUUID().toString());
        resp.setItems(List.of(t));
        resp.setTotal(1);
        resp.setPageSize(0);
        when(transportationApplicationService.list(any(SearchQuery.class))).thenReturn(resp);

        mockMvc.perform(post("/transportations/page")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(query)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items", hasSize(1)));
    }

    @Test
    void testCreateTransportationValidationError() throws Exception {
        TransportationCreateCommand invalidCommand = new TransportationCreateCommand();

        mockMvc.perform(put("/transportations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidCommand)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message[0]").isNotEmpty());
    }

    @Test
    void testUpdateTransportationThrowsDomainException() throws Exception {
        doThrow(new RouteDomainException("domain error")).when(transportationApplicationService).update(any(TransportationUpdateCommand.class));
        mockMvc.perform(post("/transportations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validUpdateCommand)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message[0]").value("domain error"));
    }

    @Test
    void testDeleteTransportationThrowsValidationException() throws Exception {
        doThrow(new ValidationException("validation error")).when(transportationApplicationService).delete(any());
        UUID id = UUID.randomUUID();
        mockMvc.perform(delete("/transportations/{id}", id))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message[0]").value("validation error"));
    }

    @Test
    void testGetTransportationThrowsGenericException() throws Exception {
        doThrow(new RuntimeException("generic error")).when(transportationApplicationService).findById(any());
        UUID id = UUID.randomUUID();
        mockMvc.perform(get("/transportations/{id}", id))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.message[0]").value("generic error"));
    }
}
